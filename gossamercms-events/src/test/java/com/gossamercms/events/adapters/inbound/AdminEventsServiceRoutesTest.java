package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.EventDto;
import com.gossamercms.events.dtos.requests.EventRequest;
import com.gossamercms.events.dtos.responses.AdminEventResponse;
import com.gossamercms.events.dtos.responses.EventLocationResponse;
import com.gossamercms.events.handlers.EventsHandler;
import com.gossamercms.events.services.EventsService;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AdminEventsServiceRoutesTest {
    @Test
    void createsAndUpdatesDetailsWithoutClashingWithInheritedRoutes() throws Exception {
        var service = mock(EventsService.class);
        var handler = mock(EventsHandler.class);
        var controller = new AdminEventsController(handler, service);
        var user = mock(CurrentJwtUser.class);
        UUID id = UUID.randomUUID();
        var existing = EventDto.builder().id(id).build();
        var response = AdminEventResponse.builder()
                .id(id).title("Event").visibility("PUBLIC").isPublished(true)
                .location(EventLocationResponse.builder().city("Toronto").build())
                .build();
        when(service.create(eq(user), any(EventRequest.class))).thenReturn(response);
        when(service.update(eq(user), eq(existing), any(EventRequest.class))).thenReturn(response);
        var conversion = new DefaultFormattingConversionService();
        conversion.addConverter(String.class, EventDto.class, value -> existing);
        var mvc = standaloneSetup(controller).setConversionService(conversion)
                .setCustomArgumentResolvers(new HandlerMethodArgumentResolver() {
                    @Override
                    public boolean supportsParameter(MethodParameter parameter) {
                        return parameter.getParameterType() == CurrentJwtUser.class;
                    }

                    @Override
                    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer container,
                                                  NativeWebRequest request, WebDataBinderFactory factory) {
                        return user;
                    }
                }).build();

        mvc.perform(post("/admin/events").contentType("application/json").content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.visibility").value("PUBLIC"))
                .andExpect(jsonPath("$.data.isPublished").value(true))
                .andExpect(jsonPath("$.data.location.city").value("Toronto"));
        mvc.perform(put("/admin/events/" + id + "/details")
                        .contentType("application/json").content("{}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.title").value("Event"));
        verify(service).create(eq(user), any(EventRequest.class));
        verify(service).update(eq(user), eq(existing), any(EventRequest.class));
        verifyNoInteractions(handler);
    }
}

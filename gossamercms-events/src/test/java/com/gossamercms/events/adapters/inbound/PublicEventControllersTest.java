package com.gossamercms.events.adapters.inbound;

import com.gossamercms.events.dtos.responses.EventTicketListResponse;
import com.gossamercms.events.handlers.EventTicketsHandler;
import com.gossamercms.mvc.controllers.TypedBaseController;
import com.gossamercms.mvc.data.ListResultset;
import com.gossamercms.mvc.data.QueryOptions;
import com.gossamercms.mvc.handlers.BaseHandler;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.ArgumentCaptor;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class PublicEventControllersTest {
    @Test
    void ticketListRequiresUserContext() throws Exception {
        var handler = mock(EventTicketsHandler.class);
        var user = mock(CurrentJwtUser.class);
        var mvc = standaloneSetup(new EventTicketsController(handler))
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

        mvc.perform(get("/event-tickets").param("userContextId", UUID.randomUUID().toString()))
                .andExpect(status().isUnauthorized());
        verifyNoInteractions(handler);
    }

    static Stream<Class<?>> controllers() {
        return Stream.of(EventPaymentsController.class, EventPromoCodesController.class,
                EventRegistrationsController.class, EventTicketsController.class,
                EventTicketTypesController.class, EventWaitlistsController.class);
    }

    @ParameterizedTest(name = "{0} exposes only mapped list/get routes")
    @MethodSource("controllers")
    void exposesReadOnlyTypedRoutes(Class<?> type) throws Exception {
        assertEquals(TypedBaseController.class, type.getSuperclass());
        var types = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        Class<?> dtoType = (Class<?>) types[1];
        Class<?> responseListType = (Class<?>) types[4];
        Object dto = dtoType.getConstructor().newInstance();
        UUID id = UUID.randomUUID();
        dtoType.getMethod("setId", UUID.class).invoke(dto, id);
        dtoType.getMethod("setCreatedAt", java.time.Instant.class)
                .invoke(dto, java.time.Instant.now());
        if (type == EventTicketsController.class) {
            dtoType.getMethod("setQrTokenHash", String.class).invoke(dto, "internal-hash");
        }
        var constructor = type.getConstructors()[0];
        Object dependency = mock(constructor.getParameterTypes()[0]);
        var handler = (BaseHandler<?, ?>) dependency;
        if (type == EventTicketsController.class) {
            var listResponse = EventTicketListResponse.builder().ticketId(id).qrTokenHash("list-hash").build();
            doReturn(ListResultset.of(List.of(listResponse), 2, 10, 1))
                    .when((EventTicketsHandler) dependency).listByUserContext(any(QueryOptions.class));
        } else {
            Object listResponseBuilder = responseListType.getMethod("builder").invoke(null);
            listResponseBuilder.getClass().getMethod("id", UUID.class).invoke(listResponseBuilder, id);
            Object listResponse = listResponseBuilder.getClass().getMethod("build").invoke(listResponseBuilder);
            doReturn(ListResultset.of(List.of(listResponse), 2, 10, 1))
                    .when(handler).getAll(any(QueryOptions.class), eq(responseListType));
        }
        Object controller = constructor.newInstance(dependency);

        var conversion = new DefaultFormattingConversionService();
        conversion.addConverter(new GenericConverter() {
            @Override
            public Set<ConvertiblePair> getConvertibleTypes() {
                return Set.of(new ConvertiblePair(String.class, dtoType));
            }

            @Override
            public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
                return dto;
            }
        });
        var user = mock(CurrentJwtUser.class);
        UUID userContextId = UUID.randomUUID();
        when(user.getUserContextId()).thenReturn(userContextId);
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
        String path = type.getAnnotation(RequestMapping.class).value()[0];

        var listRequest = get(path).param("page", "2").param("size", "10").param("status", "active");
        if (type == EventTicketsController.class) {
            listRequest.param("userContextId", UUID.randomUUID().toString());
        }
        var listResult = mvc.perform(listRequest)
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.list[0].createdAt").doesNotExist());
        var options = ArgumentCaptor.forClass(QueryOptions.class);
        if (type == EventTicketsController.class) {
            listResult.andExpect(jsonPath("$.data.list[0].ticketId").value(id.toString()))
                    .andExpect(jsonPath("$.data.list[0].id").doesNotExist())
                    .andExpect(jsonPath("$.data.list[0].qrTokenHash").value("list-hash"));
            verify((EventTicketsHandler) dependency).listByUserContext(options.capture());
            assertEquals(Map.of("status", "active", "userContextId", userContextId), options.getValue().filters());
        } else {
            listResult.andExpect(jsonPath("$.data.list[0].id").value(id.toString()))
                    .andExpect(jsonPath("$.data.list[0].qrTokenHash").doesNotExist());
            verify(handler).getAll(options.capture(), eq(responseListType));
            assertEquals(Map.of("status", "active"), options.getValue().filters());
        }
        assertEquals(2, options.getValue().page());
        assertEquals(10, options.getValue().size());

        mvc.perform(get(path + "/" + id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.toString()))
                .andExpect(jsonPath("$.data.createdAt").doesNotExist())
                .andExpect(jsonPath("$.data.qrTokenHash").doesNotExist());
        mvc.perform(post(path)).andExpect(status().isMethodNotAllowed());
        mvc.perform(put(path + "/" + id)).andExpect(status().isMethodNotAllowed());
        mvc.perform(delete(path + "/" + id)).andExpect(status().isMethodNotAllowed());
        mvc.perform(post(path + "/" + id + "/restore")).andExpect(status().isNotFound());
        mvc.perform(post(path + "/bulk")).andExpect(status().isMethodNotAllowed());
    }
}

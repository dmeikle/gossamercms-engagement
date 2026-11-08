package com.gossamercms.events.adapters.inbound;

import com.gossamercms.mvc.controllers.BaseController;
import com.gossamercms.mvc.jwt.CurrentJwtUser;
import com.gossamercms.events.dtos.EventLocationDto;
import com.gossamercms.events.handlers.EventLocationsHandler;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.MethodParameter;
import org.springframework.core.convert.converter.GenericConverter;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

class AdminEventControllersTest {
    private static final Set<String> AUDIT_FIELDS =
            Set.of("createdBy", "createdAt", "updatedAt", "deletedAt");

    static Stream<Class<?>> controllers() {
        return Stream.of(
                AdminEventCategoriesController.class,
                AdminEventCheckInsController.class,
                AdminEventLocationsController.class,
                AdminEventOrdersController.class,
                AdminEventOrganizerAssignmentsController.class,
                AdminEventOrganizerMembersController.class,
                AdminEventOrganizersController.class,
                AdminEventPaymentsController.class,
                AdminEventPromoCodesController.class,
                AdminEventRefundsController.class,
                AdminEventRegistrationsController.class,
                AdminEventsController.class,
                AdminEventSessionsController.class,
                AdminEventTicketsController.class,
                AdminEventTicketTypesController.class,
                AdminEventWaitlistsController.class);
    }

    @ParameterizedTest(name = "{0} maps request business fields")
    @MethodSource("controllers")
    void mapsRequestFieldsWithoutIdentityOrAuditFields(Class<?> type) throws Exception {
        assertEquals(BaseController.class, type.getSuperclass());
        var types = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        Class<?> dtoType = (Class<?>) types[1];
        Class<?> requestType = (Class<?>) types[2];
        Object request = populated(requestType);
        var mapper = type.getDeclaredMethod("toDto", requestType);
        mapper.setAccessible(true);
        Object dto = mapper.invoke(controller(type), request);

        Set<String> expected = fieldNames(dtoType);
        expected.removeAll(AUDIT_FIELDS);
        expected.remove("id");
        assertEquals(expected, fieldNames(requestType));
        assertMappedFields(request, dto);
        for (Field field : dtoType.getDeclaredFields()) {
            if (AUDIT_FIELDS.contains(field.getName()) || field.getName().equals("id")) {
                field.setAccessible(true);
                assertNull(field.get(dto), field.getName());
            }
        }
    }

    @ParameterizedTest(name = "{0} maps response identity and business fields")
    @MethodSource("controllers")
    void mapsResponseFieldsWithoutAuditFields(Class<?> type) throws Exception {
        var types = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        Class<?> dtoType = (Class<?>) types[1];
        Class<?> responseType = (Class<?>) types[3];
        Object dto = populated(dtoType);
        var mapper = type.getDeclaredMethod("toResponse", dtoType);
        mapper.setAccessible(true);
        Object response = mapper.invoke(controller(type), dto);

        Set<String> expected = fieldNames(dtoType);
        expected.removeAll(AUDIT_FIELDS);
        assertEquals(expected, fieldNames(responseType));
        assertMappedFields(response, dto);
    }

    @ParameterizedTest(name = "{0} serves a mapped admin response")
    @MethodSource("controllers")
    void getByIdUsesResponseMapping(Class<?> type) throws Exception {
        var types = ((ParameterizedType) type.getGenericSuperclass()).getActualTypeArguments();
        Class<?> dtoType = (Class<?>) types[1];
        Object dto = populated(dtoType);
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
        var mvc = standaloneSetup(controller(type)).setConversionService(conversion).build();
        String path = type.getAnnotation(RequestMapping.class).value()[0];
        Field id = dtoType.getDeclaredField("id");
        id.setAccessible(true);

        mvc.perform(get(path + "/" + id.get(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.id").value(id.get(dto).toString()))
                .andExpect(jsonPath("$.data.createdBy").doesNotExist())
                .andExpect(jsonPath("$.data.createdAt").doesNotExist())
                .andExpect(jsonPath("$.data.updatedAt").doesNotExist())
                .andExpect(jsonPath("$.data.deletedAt").doesNotExist());
    }

    @Test
    void locationDeleteRetainsNoContentAndDelegatesToHandler() throws Exception {
        var handler = mock(EventLocationsHandler.class);
        var user = mock(CurrentJwtUser.class);
        UUID actor = UUID.randomUUID();
        UUID locationId = UUID.randomUUID();
        when(user.getUserId()).thenReturn(actor);
        var location = EventLocationDto.builder().id(locationId).build();
        var conversion = new DefaultFormattingConversionService();
        conversion.addConverter(String.class, EventLocationDto.class, id -> location);
        var mvc = standaloneSetup(new AdminEventLocationsController(handler))
                .setConversionService(conversion)
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

        mvc.perform(delete("/admin/event-locations/" + locationId))
                .andExpect(status().isNoContent())
                .andExpect(content().string(""));

        verify(handler).deleteById(actor, locationId);
    }

    private static Object controller(Class<?> type) throws Exception {
        var constructor = type.getConstructors()[0];
        Object[] dependencies = Arrays.stream(constructor.getParameterTypes())
                .map(dependency -> mock(dependency)).toArray();
        return constructor.newInstance(dependencies);
    }

    private static Set<String> fieldNames(Class<?> type) {
        return Arrays.stream(type.getDeclaredFields()).map(Field::getName).collect(Collectors.toSet());
    }

    private static Object populated(Class<?> type) throws Exception {
        Object value = type.getConstructor().newInstance();
        for (Field field : type.getDeclaredFields()) {
            Object sample;
            if (field.getType() == UUID.class) {
                sample = UUID.randomUUID();
            } else if (field.getType() == Instant.class) {
                sample = Instant.parse("2026-11-02T12:00:00Z");
            } else if (field.getType() == String.class) {
                sample = field.getName() + "-value";
            } else if (field.getType() == Boolean.class) {
                sample = true;
            } else if (field.getType() == Integer.class) {
                sample = 7;
            } else if (field.getType() == BigDecimal.class) {
                sample = new BigDecimal("12.34");
            } else {
                throw new IllegalArgumentException("Unsupported field: " + field);
            }
            field.setAccessible(true);
            field.set(value, sample);
        }
        return value;
    }

    private static void assertMappedFields(Object source, Object target) throws Exception {
        for (Field sourceField : source.getClass().getDeclaredFields()) {
            Field targetField = target.getClass().getDeclaredField(sourceField.getName());
            sourceField.setAccessible(true);
            targetField.setAccessible(true);
            assertEquals(sourceField.get(source), targetField.get(target), sourceField.getName());
        }
    }
}

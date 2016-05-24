package com.nibado.example.microserviceversioning.model.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.nibado.example.microserviceversioning.model.Order;
import com.nibado.example.microserviceversioning.model.View;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Our completely custom Order serializer.
 */
public class OrderSerializer extends JsonSerializer<Order> {
    private static final BigDecimal HUNDRED = new BigDecimal(100);
    @Override
    public void serialize(Order order, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();

        jsonGenerator.writeNumberField("id", order.getId());
        jsonGenerator.writeStringField("customerFirstName", order.getCustomerFirstName());

        if(serializerProvider.getActiveView() == View.Version1.class) {
            jsonGenerator.writeStringField("customerLsatName", order.getCustomerLastName());
            jsonGenerator.writeNumberField("orderTotal", order.getOrderTotal().multiply(HUNDRED).longValue());
        }
        else {
            jsonGenerator.writeStringField("customerLastName", order.getCustomerLastName());
            jsonGenerator.writeNumberField("orderTotal", order.getOrderTotal());
        }

        jsonGenerator.writeNumberField("timestamp", order.getTimestamp());


        serializerProvider.defaultSerializeField("items", order.getItems(), jsonGenerator);

        jsonGenerator.writeEndObject();
    }
}

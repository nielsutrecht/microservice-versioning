package com.nibado.example.microserviceversioning;

import com.nibado.example.microserviceversioning.exceptions.NoProtocolVersionException;
import com.nibado.example.microserviceversioning.model.Order;
import com.nibado.example.microserviceversioning.model.View;
import com.nibado.example.microserviceversioning.model.adapters.OrderV1;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.nibado.example.microserviceversioning.exceptions.NotFoundException.supplierForOrder;
import static java.lang.Integer.parseInt;
import static java.lang.System.currentTimeMillis;

/**
 * Our 'order micro service'. It contains two implementations of the GET method for orders by ID:
 *
 * - GET /order/method1/{id}: Sets a view based on the supplied protocol version which is used by the serializer.
 * - GET /order/method2/{id}: Selects an adapter function based on the protocol version.
 */
@RestController
@RequestMapping("/order")
public class Controller {
    @Autowired
    private HttpServletRequest request;
    private List<Order> orderDb = new ArrayList<>();
    private Map<Integer, Class<?>> viewMap = new HashMap<>();
    private Map<Integer, Function<Order, MappingJacksonValue>> adapterMap = new HashMap<>();

    /**
     * Constructor. Creates a order 'database' (list with a single Order) and configures the view and adapter maps.
     */
    public Controller() {
        Order order;

        order = new Order(1, "John", "Williams", currentTimeMillis(), new Order.Item("Milk", 4, "2.99"), new Order.Item("Bread", 2, "3.45"));

        orderDb.add(order);

        viewMap.put(1, View.Version1.class);
        adapterMap.put(1, o -> new MappingJacksonValue(new OrderV1(o)));
    }

    /**
     * GET request that sets a View based on the protocol version and then serializes the Order to JSON.
     * @param id the ID of the Order
     * @return a serializable representation of the view.
     */
    @RequestMapping(value = "/method1/{id}", method = RequestMethod.GET)
    public MappingJacksonValue getMethod1(@PathVariable int id) {
        final MappingJacksonValue result = new MappingJacksonValue(find(id));
        final Class<?> view = getOrderView();
        result.setSerializationView(view);

        return result;
    }

    /**
     * GET request that selects an adapter based on the protocol version.
     * @param id the ID of the Order
     * @return a serializable representation of the view.
     */
    @RequestMapping(value = "/method2/{id}", method = RequestMethod.GET)
    public MappingJacksonValue getMethod2(@PathVariable int id) {
        return adapt(find(id));
    }

    /*
     * Finds an Order by ID or throws a 404.
     */
    private Order find(int id) {
        return orderDb.stream()
                .filter(o -> o.getId() == id)
                .findAny()
                .orElseThrow(supplierForOrder(id));
    }
    /*
     * View / adapter functions below are generic and would normally be in a generic
     * Filter function. For simplicity's sake we've kept them in the controller.
     */

    /*
     * Maps the protocol version to a view.
     */
    private Class<?> getOrderView() {
        return viewMap.getOrDefault(getProtocolVersion(), View.Version2.class);
    }

    /*
     * Maps the protocol version to an adapter function.
     */
    private MappingJacksonValue adapt(Order order) {
        return adapterMap
                .getOrDefault(getProtocolVersion(), MappingJacksonValue::new)
                .apply(order);
    }

    /*
     * Gets the protocol version from the header or throws a 400 exception if
     * the header is not included in the request.
     */
    private int getProtocolVersion() {
        String header = request.getHeader("X-Protocol-Version");
        if(header == null) {
            throw new NoProtocolVersionException();
        }
        else {
            return parseInt(header);
        }
    }
}

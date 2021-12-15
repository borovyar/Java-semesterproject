package com.tjv.clientside.uinterface.promptprovider;

import com.tjv.clientside.client.CustomerClient;
import com.tjv.clientside.client.EmployeeClient;
import com.tjv.clientside.client.OrderClient;
import com.tjv.clientside.client.ProductClient;
import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class CafeteriaPromptProvider implements PromptProvider {
    private final EmployeeClient employeeClient;
    private final ProductClient productClient;
    private final CustomerClient customerClient;
    private final OrderClient orderClient;


    public CafeteriaPromptProvider(EmployeeClient employeeClient, ProductClient productClient, CustomerClient customerClient, OrderClient orderClient) {
        this.employeeClient = employeeClient;
        this.productClient = productClient;
        this.customerClient = customerClient;
        this.orderClient = orderClient;
    }

    @Override
    public AttributedString getPrompt() {
        if (employeeClient.getEmployeeId() != null)
            return new AttributedString("employee " +
                    employeeClient.getEmployeeId() + " :>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.YELLOW));
        else if(productClient.getProductId() != null)
            return new AttributedString(productClient.getProductId() + " :>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.GREEN));
        else if (customerClient.getCustomerId() != null)
            return new AttributedString("customer " + customerClient.getCustomerId() + " :>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.BLUE));
        else if(orderClient.getOrderId() != null)
            return new AttributedString("order "  + orderClient.getOrderId() + " :>",
                    AttributedStyle.DEFAULT.foreground(AttributedStyle.BRIGHT));
        return new AttributedString("cafeteria:>");
    }
}

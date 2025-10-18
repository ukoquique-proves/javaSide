package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "products", layout = MainLayout.class)
@PageTitle("Productos | Javaside")
public class ProductsView extends VerticalLayout {

    public ProductsView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.PACKAGE.create(), new com.vaadin.flow.component.html.Span(" Productos"));
        Paragraph description = new Paragraph("Catálogo de productos y servicios ofrecidos por los negocios registrados.");
        Paragraph status = new Paragraph("⏳ Funcionalidad en desarrollo");
        status.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, description, status);
    }
}

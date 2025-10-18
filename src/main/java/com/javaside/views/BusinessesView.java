package com.javaside.views;

import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "businesses", layout = MainLayout.class)
@PageTitle("Negocios | Javaside")
public class BusinessesView extends VerticalLayout {

    public BusinessesView() {
        setSizeFull();
        setPadding(true);

        H2 title = new H2(VaadinIcon.SHOP.create(), new com.vaadin.flow.component.html.Span(" Negocios"));
        Paragraph description = new Paragraph("Gestión de negocios registrados en el sistema. Aquí podrás crear, editar y eliminar negocios, así como gestionar sus productos y campañas.");
        Paragraph status = new Paragraph("⏳ Funcionalidad en desarrollo - Paso 7");
        status.getStyle().set("color", "var(--lumo-secondary-text-color)");

        add(title, description, status);
    }
}

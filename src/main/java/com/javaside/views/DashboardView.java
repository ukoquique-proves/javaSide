package com.javaside.views;

import com.javaside.constants.Messages;
import com.javaside.constants.UIConstants;
import com.javaside.dto.ConnectionResultDTO;
import com.javaside.usecase.TestConnectionUseCase;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Dashboard | Javaside")
public class DashboardView extends VerticalLayout {

    private final TestConnectionUseCase testConnectionUseCase;

    public DashboardView(TestConnectionUseCase testConnectionUseCase) {
        this.testConnectionUseCase = testConnectionUseCase;
        
        setSizeFull();
        setPadding(true);
        setSpacing(true);

        // Header
        H2 title = new H2("📊 Dashboard");
        title.getStyle().set("margin", "0");
        
        Paragraph subtitle = new Paragraph("Vista general del sistema Alexia");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");
        
        add(title, subtitle);

        // Métricas principales
        HorizontalLayout metricsRow1 = new HorizontalLayout();
        metricsRow1.setWidthFull();
        metricsRow1.setSpacing(true);
        
        metricsRow1.add(
            createMetricCard("Mensajes Hoy", "0", VaadinIcon.CHAT, UIConstants.COLOR_PRIMARY_BLUE),
            createMetricCard("Leads Generados", "0", VaadinIcon.USERS, UIConstants.COLOR_SUCCESS_GREEN),
            createMetricCard("Negocios Activos", "0", VaadinIcon.SHOP, UIConstants.COLOR_WARNING_ORANGE),
            createMetricCard("Conversiones", "0", VaadinIcon.TRENDING_UP, UIConstants.COLOR_PURPLE)
        );
        
        add(metricsRow1);

        // Segunda fila de métricas
        HorizontalLayout metricsRow2 = new HorizontalLayout();
        metricsRow2.setWidthFull();
        metricsRow2.setSpacing(true);
        
        metricsRow2.add(
            createMetricCard("Campañas Activas", "0", VaadinIcon.MEGAPHONE, UIConstants.COLOR_CYAN),
            createMetricCard("Ingresos del Mes", "$0", VaadinIcon.MONEY, UIConstants.COLOR_SUCCESS_GREEN),
            createMetricCard("Clics Totales", "0", VaadinIcon.CURSOR, UIConstants.COLOR_RED),
            createMetricCard("Tasa de Respuesta", "0%", VaadinIcon.CHART_LINE, UIConstants.COLOR_DEEP_PURPLE)
        );
        
        add(metricsRow2);

        // Sección de estado del sistema
        VerticalLayout systemStatus = createSystemStatusSection();
        add(systemStatus);

        // Actividad reciente (placeholder)
        VerticalLayout recentActivity = createRecentActivitySection();
        add(recentActivity);
    }

    private VerticalLayout createMetricCard(String label, String value, VaadinIcon icon, String color) {
        VerticalLayout card = new VerticalLayout();
        card.setSpacing(false);
        card.setPadding(true);
        card.getStyle()
            .set("background", UIConstants.CARD_BACKGROUND)
            .set("border-radius", UIConstants.CARD_BORDER_RADIUS)
            .set("box-shadow", UIConstants.CARD_BOX_SHADOW)
            .set("border-left", "4px solid " + color);

        Icon cardIcon = icon.create();
        cardIcon.setSize(UIConstants.ICON_SIZE_LARGE);
        cardIcon.getStyle().set("color", color);

        H1 valueText = new H1(value);
        valueText.getStyle()
            .set("margin", "10px 0 5px 0")
            .set("font-size", "2em")
            .set("color", "#333");

        Span labelText = new Span(label);
        labelText.getStyle()
            .set("color", "var(--lumo-secondary-text-color)")
            .set("font-size", "0.9em");

        card.add(cardIcon, valueText, labelText);
        card.setAlignItems(Alignment.START);
        
        return card;
    }

    private VerticalLayout createSystemStatusSection() {
        VerticalLayout section = new VerticalLayout();
        section.setSpacing(true);
        section.setPadding(true);
        section.getStyle()
            .set("background", "white")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        H3 sectionTitle = new H3("🔌 Estado del Sistema");
        sectionTitle.getStyle().set("margin-top", "0");

        // Estado de conexiones
        HorizontalLayout statusRow = new HorizontalLayout();
        statusRow.setWidthFull();
        statusRow.setSpacing(true);

        statusRow.add(
            createStatusBadge("PostgreSQL", true),
            createStatusBadge("Telegram", false),
            createStatusBadge("WhatsApp", false),
            createStatusBadge("OpenAI/Grok", false),
            createStatusBadge("Google Places", false)
        );

        // Botón de prueba de conexión
        Button testButton = new Button("Probar Conexión a BD", VaadinIcon.REFRESH.create());
        testButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SMALL);
        testButton.addClickListener(e -> {
            ConnectionResultDTO result = testConnectionUseCase.execute();
            if (result.isSuccess()) {
                testButton.setText("✓ Conexión Exitosa");
                testButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
            } else {
                testButton.setText("✗ Error de Conexión");
                testButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
            }
        });

        section.add(sectionTitle, statusRow, testButton);
        return section;
    }

    private HorizontalLayout createStatusBadge(String service, boolean active) {
        HorizontalLayout badge = new HorizontalLayout();
        badge.setSpacing(true);
        badge.setPadding(true);
        badge.setAlignItems(Alignment.CENTER);
        badge.getStyle()
            .set("border-radius", "20px")
            .set("background", active ? "#E8F5E9" : "#FFEBEE")
            .set("padding", "5px 15px");

        Icon icon = active ? VaadinIcon.CHECK_CIRCLE.create() : VaadinIcon.CLOSE_CIRCLE.create();
        icon.setSize("16px");
        icon.getStyle().set("color", active ? "#4CAF50" : "#F44336");

        Span text = new Span(service);
        text.getStyle()
            .set("font-size", "0.9em")
            .set("font-weight", "500")
            .set("color", active ? "#2E7D32" : "#C62828");

        badge.add(icon, text);
        return badge;
    }

    private VerticalLayout createRecentActivitySection() {
        VerticalLayout section = new VerticalLayout();
        section.setSpacing(true);
        section.setPadding(true);
        section.getStyle()
            .set("background", "white")
            .set("border-radius", "8px")
            .set("box-shadow", "0 2px 4px rgba(0,0,0,0.1)");

        H3 sectionTitle = new H3("📋 Actividad Reciente");
        sectionTitle.getStyle().set("margin-top", "0");

        Paragraph placeholder = new Paragraph(Messages.NO_RECENT_ACTIVITY);
        placeholder.getStyle().set("color", "var(--lumo-secondary-text-color)");

        section.add(sectionTitle, placeholder);
        return section;
    }
}

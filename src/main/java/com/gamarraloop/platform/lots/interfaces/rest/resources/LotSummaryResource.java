package com.gamarraloop.platform.lots.interfaces.rest.resources;

/**
 * Resumen de lotes por estado para el dashboard del Confeccionista.
 * Evita que el cliente baje toda la lista y la cuente client-side.
 */
public record LotSummaryResource(
        long disponibles,
        long reservados,
        long entregados,
        long total
) {}

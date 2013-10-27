package meerkat.modules.plausible_deniability;

import meerkat.modules.IPlugin;

/**
 * Pluginy obsługujące proces niszczenia danych muszą implementować ten interfejs.
 *
 * @author Maciej Poleski
 */
public interface IOverridePlugin extends IPlugin {

    /**
     * Zwraca implementacje funkcjonalności niszczenia danych.
     */
    IOverrideImplementation getOverrideImplementation();

}

package projekt.modules.plausible_deniability;

import projekt.modules.IPlugin;

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

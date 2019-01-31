/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.clases;

import java.util.List;

/**
 *
 * @author wilme
 */
public class jsonResOpenalpr {

    private String data_type;
    private String epoch_time;
    private List<results1> results;

    public String getData_type() {
        return data_type;
    }

    public void setData_type(String data_type) {
        this.data_type = data_type;
    }

    public String getEpoch_time() {
        return epoch_time;
    }

    public void setEpoch_time(String epoch_time) {
        this.epoch_time = epoch_time;
    }

    public List<results1> getResults() {
        return results;
    }

    public void setResults(List<results1> results) {
        this.results = results;
    }

}

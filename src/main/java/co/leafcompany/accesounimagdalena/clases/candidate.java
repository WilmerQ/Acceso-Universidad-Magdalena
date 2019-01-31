/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package co.leafcompany.accesounimagdalena.clases;

/**
 *
 * @author wilme
 */
public class candidate {

    private Long matches_template;
    private Double confidence;
    private String plate;

    public Long getMatches_template() {
        return matches_template;
    }

    public void setMatches_template(Long matches_template) {
        this.matches_template = matches_template;
    }

    public Double getConfidence() {
        return confidence;
    }

    public void setConfidence(Double confidence) {
        this.confidence = confidence;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

}


package udacity.uelordi.com.popularmovies.services;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import udacity.uelordi.com.popularmovies.content.TrailerContent;

public class TrailerResponse {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<TrailerContent> results = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<TrailerContent> getResults() {
        return results;
    }

    public void setResults(List<TrailerContent> results) {
        this.results = results;
    }

}

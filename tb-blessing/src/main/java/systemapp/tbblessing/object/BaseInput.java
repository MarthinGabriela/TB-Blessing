package systemapp.tbblessing.object;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class BaseInput {
    @JsonProperty("token")
    String token;

    @JsonProperty("input")
    Object input;

    public String getToken() {
        return this.token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Object getInput() {
        return this.input;
    }

    public void setInput(Object input) {
        this.input = input;
    }

}

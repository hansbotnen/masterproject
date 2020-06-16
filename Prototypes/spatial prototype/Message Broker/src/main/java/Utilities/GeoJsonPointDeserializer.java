package Utilities;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.DoubleNode;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

public class GeoJsonPointDeserializer extends JsonDeserializer<GeoJsonPoint> {
    @Override
    public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext context)
        throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        Double longitude = (Double) ((DoubleNode) node.get("x")).doubleValue();
        Double latitude = (Double) ((DoubleNode) node.get("y")).doubleValue();
        return new GeoJsonPoint(longitude, latitude);
    }
}

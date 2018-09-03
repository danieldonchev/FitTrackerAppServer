package tracker.utils.serializers;

import com.tracker.shared.entities.GoalWeb;
import com.tracker.shared.entities.SportActivityWeb;
import com.tracker.shared.entities.WeightWeb;
import com.tracker.shared.serializers.FlatbufferSerializer;
import com.tracker.shared.serializers.GoalSerializer;
import com.tracker.shared.serializers.SportActivitySerializer;
import com.tracker.shared.serializers.WeightSerializer;

import javax.enterprise.context.RequestScoped;
import javax.enterprise.inject.New;
import javax.enterprise.inject.Produces;
import java.io.Serializable;

@RequestScoped
public class SerializerFactory implements Serializable {

    @Produces
    @RequestScoped
    @SerializerQualifier(SerializerType.Goal)
    public FlatbufferSerializer<GoalWeb> goalSerializer(@New GoalSerializer serializer){
        return serializer;
    }

    @Produces
    @RequestScoped
    @SerializerQualifier(SerializerType.SportActivity)
    public FlatbufferSerializer<SportActivityWeb> sportActivitySerializer(@New SportActivitySerializer serializer){
        return serializer;
    }

    @Produces
    @RequestScoped
    @SerializerQualifier(SerializerType.Weight)
    public FlatbufferSerializer<WeightWeb> weightSerializer(@New WeightSerializer serializer){
        return serializer;
    }


}

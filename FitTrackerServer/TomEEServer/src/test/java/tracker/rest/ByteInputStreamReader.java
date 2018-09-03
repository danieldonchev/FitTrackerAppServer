package tracker.rest;

import com.tracker.shared.entities.GoalWeb;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;

public class ByteInputStreamReader{
    private byte[] array;

    public ByteInputStreamReader(InputStream inputStream){


    }

    public interface IGoalInputStreamReader
    {
        GoalWeb Read(InputStream inputStream) throws IOException;
    }

    public class GoalInputStreamReader implements IGoalInputStreamReader {


        public GoalWeb Read(InputStream inputStream) throws IOException
        {
            return new GoalWeb().deserialize(IOUtils.toByteArray(inputStream));
        }

    }
}



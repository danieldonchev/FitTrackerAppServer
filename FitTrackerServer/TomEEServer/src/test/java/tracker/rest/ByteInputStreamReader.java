package tracker.rest;

import com.tracker.shared.Entities.GoalWeb;
import sun.misc.IOUtils;

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
            return new GoalWeb().deserialize(IOUtils.readFully(inputStream, -1, true));
        }

    }
}



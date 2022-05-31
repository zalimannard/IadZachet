package com.example.zachet2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity
{
    private TextView _postId;
    private TextView _id;
    private TextView _name;
    private TextView _email;
    private TextView _body;

    private Integer _currentComment = -1;
    private ArrayList<Comment> comments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _postId = findViewById(R.id.postId);
        _id = findViewById(R.id.id);
        _name = findViewById(R.id.name);
        _email = findViewById(R.id.email);
        _body = findViewById(R.id.body);
        String url = "https://jsonplaceholder.typicode.com/comments";
        new GetUrlData().execute(url);
    }


    private static float startX = 0;
    private static float startY = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        if (event.getAction() == 0)
        {
            startX = event.getX();
            startY = event.getY();
        } else if (event.getAction() == 1)
        {
            if (Math.abs(event.getX() - startX) > 300)
            {
                if (startX > event.getX())
                {
                    setCurrentComment(Math.min(
                            comments.size() - 1,
                            getCurrentComment() + 1
                    ));
                    updateValues();
                } else
                {
                    setCurrentComment(Math.max(
                            0,
                            getCurrentComment() - 1
                    ));
                    updateValues();
                }
            }
            startX = 0.0f;
            startY = 0.0f;
        }
        return super.onTouchEvent(event);
    }

    private class GetUrlData extends AsyncTask<String, String, String>
    {
        protected void onPreExecute()
        {
            super.onPreExecute();
            _postId.setText("Post ID:;");
            _id.setText("ID: ");
            _name.setText("Name: ");
            _email.setText("Email: ");
            _body.setText("Body: ");
        }

        @Override
        protected String doInBackground(String... strings)
        {
            HttpURLConnection connection = null;
            BufferedReader reader = null;
            try
            {
                URL url = new URL(strings[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream stream = connection.getInputStream();
                reader = new BufferedReader(new InputStreamReader(stream));

                StringBuffer buffer = new StringBuffer();
                String line = "";

                while ((line = reader.readLine()) != null)
                {
                    buffer.append(line).append("\n");
                }
                return buffer.toString();
            } catch (MalformedURLException e)
            {
                e.printStackTrace();
            } catch (IOException e)
            {
                e.printStackTrace();
            } finally
            {
                if (connection != null)
                    connection.disconnect();
                try
                {
                    if (reader != null)
                    {
                        reader.close();
                    }
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);
            try
            {
                JSONArray obj = new JSONArray(result);

                for (int i = 0; i < obj.length(); ++i)
                {
                    Comment comment = new Comment(
                            obj.getJSONObject(i).getString("postId"),
                            obj.getJSONObject(i).getString("id"),
                            obj.getJSONObject(i).getString("name"),
                            obj.getJSONObject(i).getString("email"),
                            obj.getJSONObject(i).getString("body")
                    );
                    comments.add(comment);
                    if (getCurrentComment() == -1)
                    {
                        _currentComment = 0;
                        updateValues();
                    }
                }
            } catch (JSONException e)
            {

            }
        }
    }

    private void updateValues()
    {
        if (getCurrentComment() != -1)
        {
            _postId.setText("PostId: " + comments.get(getCurrentComment()).getPostId());
            _id.setText("Id: " + comments.get(getCurrentComment()).getId());
            _name.setText("Name: " + comments.get(getCurrentComment()).getName());
            _email.setText("Email: " + comments.get(getCurrentComment()).getEmail());
            _body.setText("Body: " + comments.get(getCurrentComment()).getBody());
        }
    }

    private Integer getCurrentComment()
    {
        return _currentComment;
    }

    private void setCurrentComment(Integer value)
    {
        _currentComment = value;
    }
}
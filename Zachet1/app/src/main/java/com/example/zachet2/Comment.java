package com.example.zachet2;

public class Comment
{
    private String _postId;
    private String _id;
    private String _name;
    private String _email;
    private String _body;

    public Comment(String postId, String id, String name, String email, String body)
    {
        setPostId(postId);
        setId(id);
        setName(name);
        setEmail(email);
        setBody(body);
    }

    public String getPostId()
    {
        return _postId;
    }

    public void setPostId(String value)
    {
        _postId = value;
    }

    public String getId()
    {
        return _id;
    }

    public void setId(String value)
    {
        _id = value;
    }

    public String getName()
    {
        return _name;
    }

    public void setName(String value)
    {
        _name = value;
    }

    public String getEmail()
    {
        return _email;
    }

    public void setEmail(String value)
    {
        _email = value;
    }

    public String getBody()
    {
        return _body;
    }

    public void setBody(String value)
    {
        _body = value;
    }
}

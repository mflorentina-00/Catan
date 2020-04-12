package catan.API.request;

import catan.API.Response;
import catan.API.util.RandomString;
import catan.Application;

public interface GameRequest {
    RandomString randString = new RandomString();
    Application obj = new Application();
    Response run();
}

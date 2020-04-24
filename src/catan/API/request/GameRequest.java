package catan.API.request;

import catan.API.Response;
import catan.API.util.RandomString;

public interface GameRequest {
    RandomString randString = new RandomString();
    Response run();
}

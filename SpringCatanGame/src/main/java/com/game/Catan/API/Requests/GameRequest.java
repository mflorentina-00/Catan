package com.game.Catan.API.Requests;

import com.game.Catan.API.BaseResponse;
import com.game.Catan.API.RandomString;
import com.game.Catan.CatanApplication;

import java.util.concurrent.ThreadLocalRandom;

public interface GameRequest {
    RandomString randString = new RandomString();
    CatanApplication obj = new CatanApplication();
    BaseResponse run();
}

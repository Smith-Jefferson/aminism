package org.aminism.spider.aminism.model;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ygxie
 * @date 2019/1/27.
 */
public class ReateScoreModel {
    private float rate;
    private List<Score> scores;

    public ReateScoreModel() {
        scores = new ArrayList<>();
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

    public void setRateStr(String rate) {
        if(StringUtils.isBlank(rate)){
            setRate(0);
        }else {
            setRate(Float.parseFloat(rate));
        }
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }

    public ReateScoreModel paser(String ratescore){
        Map<String,String> map = JSON.parseObject(ratescore,new TypeReference<HashMap<String,String>>(){});
        for (Map.Entry<String, String> entry : map.entrySet()) {
            if(entry.getKey().equals("ratenum")){
                this.setRateStr(entry.getValue());
            }else{
                Score score = new Score();
                score.setScore(entry.getKey());
                score.setScoreWeightStr(entry.getValue());
                this.getScores().add(score);
            }
        }
        return this;
    }
}
class Score{
    private String score;
    private Float scoreWeight;

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public Float getScoreWeight() {
        return scoreWeight;
    }

    public void setScoreWeight(Float scoreWeight) {
        this.scoreWeight = scoreWeight;
    }

    public void setScoreWeightStr(String scoreWeight) {
        if(StringUtils.isBlank(scoreWeight)){
            this.scoreWeight =Float.valueOf(0);
        }else{
            this.scoreWeight = Float.parseFloat(scoreWeight.replace("%","" ));
        }
    }
}
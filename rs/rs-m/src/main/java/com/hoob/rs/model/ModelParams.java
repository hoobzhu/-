package com.hoob.rs.model;

import com.hoob.rs.comm.model.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

@Entity
@Table(name = "model_params",indexes={
        @Index(columnList="modelName",unique=true)})
public class ModelParams extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private String modelName;//模型名称
    private int rank;//模型中隐含因子的个数
    private double regParam;//正则化参数regParam：和其他机器学习算法一样，控制模型的过拟合情况
    private Integer maxIter;//最大迭代次数

    @Column(columnDefinition = "varchar(20) comment '模型名称'")
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    @Column(columnDefinition = "int(11) not null default 8  comment '模型中隐含因子的个数'")
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    @Column(columnDefinition = "double default 0.8  comment '正则化参数regParam'")
    public double getRegParam() {
        return regParam;
    }

    public void setRegParam(double regParam) {
        this.regParam = regParam;
    }

    @Column(columnDefinition = "int(11) not null default 10  comment '最大迭代次数'")
    public Integer getMaxIter() {
        return maxIter;
    }

    public void setMaxIter(Integer maxIter) {
        this.maxIter = maxIter;
    }
}

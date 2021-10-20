---
title: CP630 Project Proposal <br> Tool Wear Prediction
author: Adam Fortier
date: Nov 28, 2020
---

<link rel="stylesheet" type="text/css" media="all" href="proposal.css" />

## Introduction

For CNC milling machines, tool wear can have a significant effect on the quality of product being produced.  A typical milling machine will move along 3-axes, with a fourth axis, the spindle, that controls the speed of the tool.  The tool is the wearable component that cuts the material.  A tool is given a rating by the tool manufacturer based on the time spent running at the rated speed.  This value is used by the CNC controller to evaluate if the tool is usable.  This is a simple approach to evaluating tool life, but it does not take into account any other factors that can affect tool life. 

The purpose of this project is to use data collected from a CNC machine to develop a model that can predict if a tool is worn based on the current running parameters of the machine.  These paramters include, but are not limited to, spindle temperature, spindle speed, power outpur, axis speed and axis position.  The prediction will be displayed to the tooling technician so that they can prepare for replacement of work tools.  The dataset source is publicly available via the Kaggle website and was populated by the University of Michigan Smart Lab.  Weka API will be used to impletment the machine learning algorithms

I am currently implementing a tool management system at my place of employment, with the goal of distinguishing between worn and unworn tools.  We currently have no way of telling how long a tool is used and rely on visual inspection.  To be on the safe side, tools are frequently disposed of before they reach a worn state.  This leads to significant expenses on new tools that could be avoided.

## Problem solving and algorithms

The dataset is the coolected from 18 experiments conducted on a CNC milling maching, with varying tool conditions, speeds and clamping pressure.  All experiments used the same wax block and cutting the same pattern.  The input features are material, feed rate(spindle speed) and clamping pressure.  The outputs collected are the tool condition (worn/unworn), machining completed and whether or not the tool was worn after completion.  This output data is good for classification machine learning alogorithms.  The dataset contains data collected every 100ms and includes all measurable features of the operation.  This includes power, speed, acceleration and positioning feedback.  Since time series data is being collected, an additional output could be calculated which is the time remaining before failure.  The WEKA library will be used to build models using logistic regression, support vector machine, k-nearest neighbour and decision tree methods.

## Proposed System Design

### System components
- Login page.
- Admin user login. Allows admin to compare the performance of 2-3 machine learning algorithms.
- Admin user login. Selects best performed algorithm, builds the model, and saves the model into database.
- Regular user login. User selects a specific tool or all tools to display predictions
- About Us page.

### Preliminary technical tools
The project will be developed based on the EC components from the labs and assignments of the course, and use the following basic tools.

- Eclipse
- MySql
- Java EE
- Weka API
- Wildfly
- Spring
- Docker

## Project plan and schedule
The following is the proposed plan for implementation:

| Task ID       | Description     | Due Date     |
| :------------- | :----------: | -----------: |
|  1 | Project Proposal   | Day 6 of week 11    |
|  2 | Problem solving and dataset |	Day 6 of week 11  |
|  3 | Algorithms and model generation |	Day 6 of week 12  |
|  4 | Admin component development |	Day 6 of week 13  |
|  4 | User component development |	Day 6 of week 14  \| |

## References

1. Data source:  https://www.kaggle.com/saeedsh91/cnc-tool-wear
2. Weka 3: Machine Learning Software in Java. https://www.cs.waikato.ac.nz/ml/weka/
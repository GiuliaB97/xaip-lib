# XAIP-lib

*Authors:
[Giulia Brugnatti](https://github.com/giuliab97)*


**Some quick links**:
- [Documentation](https://giuliab97.github.io/xaip-lib/) (work in progress)

## Intro

This project, developed as part of the Master Thesis for the MSc in Computer Science and Engineering, consists of a pure Kotlin library for eXplainable AI planning (XAIP).

The project aims to implement a software proposal to create a model-based contrastive explanation within the XAIP domain.

Within this framework, the project is made up of four modules that will be described in the next lines:

- the `planning` module is a self-contained module containing the core entities to design and solve a planning problem.
Its abstractions are exploited within the different modules of the library to guarantee the coherence of the proposal.

- the key module of the library is the `explanation` module; it provides the core entities to enable users to inquire a planner about its decisions and retrieve explanations about them.

- the `dsl` module is a utility module designed to enhance the usage of the planning module.

- the `domain` module containing the implementation of two planning domains: the Block World and the Logistics domains.


## For users

This project is a work in progress, not yet intended for general-purpose usage.

## Experiments

### Proof of concept demonstration

We design a set of usage examples of the framework for each domain defined.
The end-to-end examples can be retrieved at directory `evaluation/src/test/kotlin/endtoendexample` and show how to create and instantiate domains and problems and also how to ask the system explanation about plans both exploiting the dsl and without using it.
The execution of each example produces either a *general* explanation, or a *minimal* one.

In the next lines, we will describe the general idea of the framework usage, besides giving some practical examples about how to enquire the system about its decisions and the kind of output a user should aspect.
In general, we can summarize the core steps the user must perform to get an explanation as follows:
- to begin, the user defines a model for a problem and a domain[^1];
- the user then instantiates an appropriate question specifying its parameters; commonly: the problem and the domain defined in the previous step, the plan retrieved from the automatic planner and the user's suggestions;
- consequently, the user must instantiate an `Explainer`; thus, the component responsible for building an explanation from a given question and an `ExplanationPresenter`, which is the entity entitled to show the explanation in a user-friendly way.

For example, let's assume we want to enquire the system about a solution proposed for a transportation problem within the Logistics domain.
We assume a user wants to move some objects that we call containers in a given location and wants to exploit an agent, which we call a robot, to do so.
More specifically we call:
- `l1`, ..., `l7`: the locations. Particularly, we arrange the locations as a graph shown following figure:
![logisticsGraph](https://user-images.githubusercontent.com/60556203/221888187-64ebd69f-6c67-43ba-a650-5a8549e6aed2.png)

- `c1` and `c2`: the two containers, that are respectively in location `l2` and `l3`;
- `r`: the robot agent that must move the containers `c1` and `c2` from their former locations to `l4` and `l1` respectively and eventually reach `l5`.

We assume that the initial solution proposed by the planner was:
```
val formerPlan = Plan.of(
            listOf(
                moveRfromL1toL3,
                loadC2fromL3onR,
                moveRfromL3toL1,
                unloadC2fromRtoL1,
                moveRfromL1toL2,
                loadC1fromL2onR,
                moveRfromL2toL4,
                unloadC1fromRtoL4,
                moveRfromL4toL5
            )
        )
```
Whereas a user wants to know why the robot did not go to `l6` to arrive at `l5`.
To force the system to check if there is a feasible solution for the robot to go to `l6` instead of `l4`, the user should define an appropriate question, `QuestionReplaceOperator`, instantiating it with the proper parameters as follows:
```
val question = QuestionReplaceOperator (
                problem ,
                formerPlan ,
                moveRfromL4toL6 ,
                8 ,
                alternativeState
              )
```
Next, create an `Explainer` and initialize it with a `Planner`:
```
val explainer = Explainer.of(Planner.strips())
```

asking the `Explainer` to explain the question:
```
val explanation = explainer.explain(question)
```
Finally, the user should instantiate an appropriate `ExplanationPresent` according to the type of explanation the user would like to receive. In this case, we assume the user would like a general explanation; to this extent, the user can create a `ExplanationPresenter` and require it to display the explanation:
```
ExplanationPresenter.of(explation).present()
```
In the next lines we see the output of this first call:
```
The problem [atLocation(r, l5), inContainerLocation(c1, l4), inContainerLocation(c2, l1)] is solvable.
The former plan was: [move(r, l1, l3), load(l3, c2, r), move(r, l3, l1),
    unload(l1, c2, r), move(r, l1, l2), load(l2, c1, r), move(r, l2, l4), 
    unload(l4, c1, r), move(r, l4, l5)]
The novel plan is: [move(r, l1, l3), load(l3, c2, r), move(r, l3, l1),
    unload(l1, c2, r), move(r, l1, l2), load(l2, c1, r), move(r, l2, l4), 
    unload(l4, c1, r), move(r, l4, l6), move(r, l6, l5)].
The novel plan is a valid solution to the problem.
The minimal solution is: [move(r, l1, l3), load(l3, c2, r), move(r, l3, l1), 
    unload(l1, c2, r), move(r, l1, l2), load(l2, c1, r), move(r, l2, l4), 
    unload(l4, c1, r), move(r, l4, l5)]
The plan is not the minimal solution.

There are 2 additional operators with respect to the minimal solution: 
    [move(r, l4, l6), move(r, l6, l5)].
```
The explanation demonstrates that it is possible to reach `l5` from `l6`. However, one can notice that a general explanation can be quite verbose whereas a user may want a more concise answer.
In such cases, the user can ask the system for a simplified version of the general explanation by calling \texttt{pre\-sentMi\-ni\-malEx\-pla\-na\-tion()} obtaining the following output:
```
The plan: [move(r, l1, l3), load(l3, c2, r), move(r, l3, l1), unload(l1, c2, r), move(r, l1, l2), load(l2, c1, r), move(r, l2, l4), unload(l4, c1, r), 
    move(r, l4, l6), move(r, l6, l5)], is valid: true
 The length is acceptable: true
 Operators missing: [move(r, l4, l5)]
 Additional operators: [move(r, l4, l6), move(r, l6, l5)]
``` 
The two explanations provide a similar insight into the property of the solution proposed, but the second one presents it more concisely.
To this extent, we design *minimal explanations* for proficient users who may not be interested in a detailed explanation in natural language.

[^1]: A set of problems for each domain defined is already available in the `domain` module.

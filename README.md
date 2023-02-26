# XAIP-lib

*Authors:
[Giulia Brugnatti](https://github.com/giuliab97)*


**Some quick links**:
- [Documentation](https://giuliab97.github.io/xaip-lib/) (work in progress)

## Intro

This project, developed as part of the Master Thesis for the Msc in Computer Science and Engineering, consists of a pure Kotlin library for eXplainable AI planning (XAIP).

The aim of the project is to implement a software proposal to create model-based contrastive explanation within the XAIP domain.

Within this framework, the project is made up of four modules that will be described in the next lines:

- the `planning` module is a self-contained modules containing the core entities to design and solve a planning problem.
Its abstractions are exploited within the different modules of the library to guarantee the coherence of the proposal.

- the key module of the library is the `explanation` module; it provides the core entities to enable users to inquiry a planner about its decisions and retrieve explanation about them.

- the `dsl` module is a utility moduly design enhance the usage of the planning module.

- the `domain` module containing the implementation of two planning domains: the Block World and the Logistics domains.


## For users

This project is a work in progress, not yet intended for general purpose usage.

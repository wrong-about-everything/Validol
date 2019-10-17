.. toctree::
   :titlesonly:

   declarative_validation
   context_specific_validation
   extendable_validation

.. contents::
   declarative_validation
   context_specific_validation
   extendable_validation

The Why
=========

[Link to library]

There are quite a few libraries for data validation. Why another one?


It's re-usable
------------------------------------
Each piece of validation logic is an object which can decorate existing validatable elements.

No magic and hidden dependencies
------------------------------------
You're encouraged to inject all dependencies through constructor. The resulted code is highly testable, readable and predictable.

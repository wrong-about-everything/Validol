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

Postel's law
--------------
User stories can accept any data. What they validate is only the data they actually are able to work with.
Validation doesn't postulate a strict schema. So if there is some field that is not needed, validation would pass.

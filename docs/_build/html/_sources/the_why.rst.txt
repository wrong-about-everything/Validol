The Why
=========

There are quite a few libraries for data validation. Why `another one <https://github.com/wrong-about-everything/Validol>`_?

It's declarative
-----------------
I'd rather say, it's :doc:`truly declarative <why/declarative_validation>`.
Not put-all-my-dependencies-in-config-file-and-i'm-done-kind of declarative, but (hopefully) the real one.

It's contextual
-----------------
Second, it encourages a :doc:`contextual validation <why/context_specific_validation>`, which is contrary to data-model-level validation.
And, sorrowfully, which is the default mode in most frameworks.
Anyways, you're not forced to use it and you could opt into the
`best-practices-compliant <https://martinfowler.com/bliki/ContextualValidation.html>`_
`library <https://github.com/wrong-about-everything/Validol>`_ instead.

It's infinitely extendable
---------------------------
Two concepts contribute to infinite extendability. I've already covered the first one, namely it's declarative style.
The second one is high composability. All the validatable elements implement the single ``Validatable<T>`` interface.
If you have extremely complex logic, you won't drown in spaghetti code. Instead, you'll have a nice object composition.
Check an :doc:`example here <why/context_specific_validation>`, in
"Contextual validation that is specific to a concrete user story" sub-section.

It's re-usable
------------------------------------
Each piece of validation logic is an object which can decorate existing validatable elements.
Reusability was never an ultimate goal for me, but it's a nice side-effect to have.

No magic and hidden dependencies
------------------------------------
You're encouraged to `inject all dependencies <https://codeburst.io/static-classes-are-evil-or-make-your-dependencies-explicit-af3e73bd29dd>`_
through constructor. The resulted code is highly testable, readable and predictable.

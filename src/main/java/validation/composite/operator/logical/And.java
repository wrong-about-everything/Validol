package validation.composite.operator.logical;

import validation.Validatable;

public class And<X, Y> implements Validatable<X, Y>
{
    private Validatable<X> left;
    private Validatable<Y> right;

    public And(Validatable<X> left, Validatable<Y> right)
    {
        $this->left = $left;
        $this->right = $right;
    }

    public function result(): Result
    {
        $leftResult = (new Required($this->left))->result();
        $rightResult = (new Required($this->right))->result();

        if (!$leftResult->isSuccessful() || !$rightResult->isSuccessful()) {
            return
                new Named(
                    'global',
                    new Scalar(sprintf('Either %s and %s is required', $leftResult->name(), $rightResult->name())),
                    false
                );
        }

        return new Named('global', new Scalar('Successful result'),true);
    }
}

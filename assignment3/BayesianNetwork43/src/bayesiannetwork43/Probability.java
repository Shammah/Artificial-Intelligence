package bayesiannetwork43;

/**
 * General probability utility class.
 *
 * Contains basic probability operations and assertions, like 0 <= p <= 1.
 *
 * @author Group 43
 */
public class Probability {

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + (int) (Double.doubleToLongBits(this._value) ^
                (Double.doubleToLongBits(this._value) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final Probability other = (Probability) obj;

        if (Double.doubleToLongBits(this._value) != Double.doubleToLongBits(other._value)) {
            return false;
        }

        return true;
    }
    private double _value;

    /**
     * Constructor.
     * @param p The probability.
     * @pre 0 <= p <= 1
     */
    public Probability(double p) {
        setValue(p);
    }

    /**
     * Returns the value of the probability itself.
     * @return the probability.
     */
    public double getValue() {
        return _value;
    }

    /**
     * Sets the value of the probability.
     * @param p The new probability.
     * @throws IllegalArgumentException The probability is not in the range [0, 1]
     */
    public void setValue(double p) throws IllegalArgumentException {
        if (p < 0 || p > 1) {
            throw new IllegalArgumentException("Probability <" +
                    toString() + "> is not in the range [0, 1].");
        }

        _value = p;
    }

    /**
     * Calculates the complement of a probability and returns a new probability.
     * @return A new complemented probability.
     */
    public Probability complement() {
        return new Probability(1.0 - getValue());
    }

    /**
     * Adds two probabilities together.
     * @param p The probability to add to the current one.
     * @return The sum of the probabilities as a new probability.
     */
    public Probability add(Probability p) {
        return new Probability(getValue() + p.getValue());
    }

    @Override
    public String toString() {
        return "" + getValue();
    }

    /**
     * Returns {@code this} probability times {@code prob}.
     * @param prob the {@code Probability} to be multiplied with
     * @return a {@code Probability} with value {@code this._value * prob._value}
     */
    public Probability times(Probability prob) {
        return new Probability(prob._value * this._value);
    }
}

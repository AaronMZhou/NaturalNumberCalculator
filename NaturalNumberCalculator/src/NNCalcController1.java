import components.naturalnumber.NaturalNumber;
import components.naturalnumber.NaturalNumber2;

/**
 * Controller class.
 *
 * @author Aaron Zhou
 */
public final class NNCalcController1 implements NNCalcController {

    /**
     * Model object.
     */
    private final NNCalcModel model;

    /**
     * View object.
     */
    private final NNCalcView view;

    /**
     * Useful constants.
     */
    private static final NaturalNumber TWO = new NaturalNumber2(2),
            INT_LIMIT = new NaturalNumber2(Integer.MAX_VALUE);

    /**
     * Updates this.view to display this.model, and to allow only operations
     * that are legal given this.model.
     *
     * @param model
     *            the model
     * @param view
     *            the view
     * @ensures [view has been updated to be consistent with model]
     */
    private static void updateViewToMatchModel(NNCalcModel model, NNCalcView view) {
        view.updateTopDisplay(model.top());
        view.updateBottomDisplay(model.bottom());
        // subtract allowed iff bottom ≤ top
        view.updateSubtractAllowed(model.bottom().compareTo(model.top()) <= 0);
        // divide allowed iff bottom > 0
        view.updateDivideAllowed(!model.bottom().isZero());
        // power allowed iff bottom ≤ INT_LIMIT
        view.updatePowerAllowed(model.bottom().compareTo(INT_LIMIT) <= 0);
        // root allowed iff 2 ≤ bottom ≤ INT_LIMIT
        view.updateRootAllowed(model.bottom().compareTo(TWO) >= 0
                && model.bottom().compareTo(INT_LIMIT) <= 0);
    }

    /**
     * Constructor.
     *
     * @param model
     *            model to connect to
     * @param view
     *            view to connect to
     */
    public NNCalcController1(NNCalcModel model, NNCalcView view) {
        this.model = model;
        this.view = view;
        updateViewToMatchModel(model, view);
    }

    @Override
    public void processClearEvent() {
        /*
         * Get alias to bottom from model
         */
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        bottom.clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSwapEvent() {
        /*
         * Get aliases to top and bottom from model
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        /*
         * Update model in response to this event
         */
        NaturalNumber temp = top.newInstance();
        temp.transferFrom(top);
        top.transferFrom(bottom);
        bottom.transferFrom(temp);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processEnterEvent() {
        /*
         * Copy bottom into top
         */
        this.model.top().copyFrom(this.model.bottom());
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddEvent() {
        /*
         * Compute #this.model.top + #this.model.bottom, put result in bottom;
         * clear top.
         */
        NaturalNumber sum = this.model.top().newInstance();
        sum.copyFrom(this.model.top());
        sum.add(this.model.bottom());
        this.model.bottom().transferFrom(sum);
        this.model.top().clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processSubtractEvent() {
        /*
         * Compute #this.model.top - #this.model.bottom, put result in bottom;
         * clear top.
         */
        NaturalNumber diff = this.model.top().newInstance();
        diff.copyFrom(this.model.top());
        diff.subtract(this.model.bottom());
        this.model.bottom().transferFrom(diff);
        this.model.top().clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processMultiplyEvent() {
        /*
         * Compute #this.model.top * #this.model.bottom, put result in bottom;
         * clear top.
         */
        NaturalNumber prod = this.model.top().newInstance();
        prod.copyFrom(this.model.top());
        prod.multiply(this.model.bottom());
        this.model.bottom().transferFrom(prod);
        this.model.top().clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processDivideEvent() {
        /*
         * Divide: bottom must be > 0 After divide, model.top holds quotient,
         * remainder returned. We need bottom := quotient, top := remainder.
         */
        NaturalNumber top = this.model.top();
        NaturalNumber bottom = this.model.bottom();
        NaturalNumber remainder = top.divide(bottom);
        bottom.transferFrom(top);
        top.transferFrom(remainder);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processPowerEvent() {
        /*
         * Compute (#this.model.top) ^ (#this.model.bottom), put result in
         * bottom; clear top.
         */
        NaturalNumber pow = this.model.top().newInstance();
        pow.copyFrom(this.model.top());
        int exponent = this.model.bottom().toInt();
        pow.power(exponent);
        this.model.bottom().transferFrom(pow);
        this.model.top().clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processRootEvent() {
        /*
         * Compute floor-root: floor((#this.model.top)^(1/#this.model.bottom)),
         * put result in bottom; clear top.
         */
        NaturalNumber rt = this.model.top().newInstance();
        rt.copyFrom(this.model.top());
        int r = this.model.bottom().toInt();
        rt.root(r);
        this.model.bottom().transferFrom(rt);
        this.model.top().clear();
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

    @Override
    public void processAddNewDigitEvent(int digit) {
        /*
         * Multiply bottom by 10 and add the new digit in one step: bottom :=
         * #bottom * 10 + digit
         */
        this.model.bottom().multiplyBy10(digit);
        /*
         * Update view to reflect changes in model
         */
        updateViewToMatchModel(this.model, this.view);
    }

}

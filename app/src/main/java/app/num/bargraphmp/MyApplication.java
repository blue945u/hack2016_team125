package app.num.bargraphmp;

import android.app.Application;

/**
 * Created by Emily on 2016/8/21.
 */
public class MyApplication extends Application {

    public int firsttime=1;
    private String someVariable;

    public String getSomeVariable() {
        return someVariable;
    }

    public void setSomeVariable(String someVariable) {
        this.someVariable = someVariable;
    }
}

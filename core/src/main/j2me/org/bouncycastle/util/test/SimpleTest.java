package org.bouncycastle.util.test;

import java.io.PrintStream;
import java.util.Enumeration;
import java.util.Vector;

import org.bouncycastle.util.Arrays;

public abstract class SimpleTest
    implements Test
{
    public abstract String getName();

    private TestResult success()
    {
        return SimpleTestResult.successful(this, "Okay");
    }
    
    protected void fail(
        String message)
    {
        throw new TestFailedException(SimpleTestResult.failed(this, message));
    }
    
    protected void fail(
        String    message,
        Throwable throwable)
    {
        throw new TestFailedException(SimpleTestResult.failed(this, message, throwable));
    }
    
    protected void fail(
        String message,
        Object expected,
        Object found)
    {
        throw new TestFailedException(SimpleTestResult.failed(this, message, expected, found));
    }
        
protected void isTrue(
        boolean value)
    {
        if (!value)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
        }
    }

    protected void isTrue(
        String message,
        boolean value)
    {
        if (!value)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }
    }

    protected void isEquals(
        Object a,
        Object b)
    {
        if (!a.equals(b))
        {
            throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
        }
    }

    protected void isEquals(
        int a,
        int b)
    {
        if (a != b)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, "no message"));
        }
    }

    protected void isEquals(
        String message,
        boolean a,
        boolean b)
    {
        if (a != b)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }
    }

    protected void isEquals(
        String message,
        long a,
        long b)
    {
        if (a != b)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }
    }

    protected void isEquals(
        String message,
        Object a,
        Object b)
    {
        if (a == null && b == null)
        {
            return;
        }
        else if (a == null)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }
        else if (b == null)
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }

        if (!a.equals(b))
        {
            throw new TestFailedException(SimpleTestResult.failed(this, message));
        }
    }

    protected boolean areEqual(
        byte[] a,
        byte[] b)
    {
        return Arrays.areEqual(a, b);
    }
    
    public TestResult perform()
    {
        try
        {
            performTest();
            
            return success();
        }
        catch (TestFailedException e)
        {
            return e.getResult();
        }
        catch (Exception e)
        {
            return SimpleTestResult.failed(this, "Exception: " +  e, e);
        }
    }
    
    protected static void runTest(
        Test        test)
    {
        runTest(test, System.out);
    }
    
    protected static void runTest(
        Test        test,
        PrintStream out)
    {
        TestResult      result = test.perform();

        out.println(result.toString());
        if (result.getException() != null)
        {
            result.getException().printStackTrace();
        }
    }

    public static void runTests(Test[] tests)
    {
        runTests(tests, System.out);
    }

    public static void runTests(Test[] tests, PrintStream out)
    {
        Vector failures = new Vector();
        for (int i = 0; i != tests.length; i++)
        {
            TestResult result = tests[i].perform();
            if (!result.isSuccessful())
            {
                failures.addElement(result);
            }

            if (result.getException() != null)
            {
                result.getException().printStackTrace();
            }

            out.println(result);
        }

        out.println("-----");
        if (failures.isEmpty())
        {
            out.println("All tests successful.");
        }
        else
        {
            out.println("Completed with " + failures.size() + " FAILURES:");

            Enumeration e = failures.elements();
            while (e.hasMoreElements())
            {
                System.out.println("=>  " + (TestResult)e.nextElement());
            }
        }

    }

    public abstract void performTest()
        throws Exception;
}

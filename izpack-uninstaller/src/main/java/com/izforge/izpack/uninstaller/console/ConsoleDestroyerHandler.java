package com.izforge.izpack.uninstaller.console;

import com.izforge.izpack.api.data.LocaleDatabase;
import com.izforge.izpack.api.handler.AbstractUIHandler;
import com.izforge.izpack.api.handler.AbstractUIProgressHandler;

/**
 * The destroyer handler.
 */
public class ConsoleDestroyerHandler implements AbstractUIProgressHandler
{
    private int AUTO_ANSWER_MODE = -2;

    /**
     * The locale-specific messages.
     */
    private final LocaleDatabase messages;


    public ConsoleDestroyerHandler(LocaleDatabase messages)
    {
        this.messages = messages;
    }

    private void out(String str)
    {
        System.out.println(str);
    }

    private boolean askOKCancel(String question, int defaultchoice)
    {
        if (defaultchoice == AUTO_ANSWER_MODE)
        {
            return true;
        }
        boolean defaultanswer = defaultchoice == 1;
        try
        {
            System.out.print(question + " (Ok/Cancel) [" + (defaultanswer ? "O" : "C") + "]:");
            String rline = readln();
            if (rline.toLowerCase().startsWith("o"))
            {
                return true;
            }
            if (rline.toLowerCase().startsWith("c"))
            {
                return false;
            }
        }
        catch (Exception e)
        {
        }
        if (defaultchoice == -1)
        {
            return askOKCancel(question, defaultchoice);
        }
        return defaultanswer;
    }

    private int askYesNoCancel(String question, int defaultchoice)
    {
        if (defaultchoice == AUTO_ANSWER_MODE)
        {
            return AbstractUIHandler.ANSWER_YES;
        }
        boolean defaultanswer = defaultchoice == 1;
        try
        {
            System.out.print(question + " (Yes/No/Cancel) [" + (defaultanswer ? "Y" : "N") + "]:");
            String rline = readln();
            if (rline.toLowerCase().equals("y"))
            {
                return AbstractUIHandler.ANSWER_YES;
            }
            if (rline.toLowerCase().equals("n"))
            {
                return AbstractUIHandler.ANSWER_NO;
            }
            if (rline.toLowerCase().equals("c"))
            {
                return AbstractUIHandler.ANSWER_CANCEL;
            }
        }
        catch (Exception e)
        {
        }
        if (defaultchoice == -1)
        {
            return askYesNoCancel(question, defaultchoice);
        }
        return defaultchoice;
    }

    private int askYesNo(String question, int defaultchoice)
    {
        if (defaultchoice == AUTO_ANSWER_MODE)
        {
            return AbstractUIHandler.ANSWER_YES;
        }
        boolean defaultanswer = defaultchoice == 1;
        try
        {
            System.out.print(question + " (Yes/No) [" + (defaultanswer ? "Y" : "N") + "]:");
            String rline = readln();
            if (rline.toLowerCase().equals("y"))
            {
                return AbstractUIHandler.ANSWER_YES;
            }
            if (rline.toLowerCase().equals("n"))
            {
                return AbstractUIHandler.ANSWER_NO;
            }
        }
        catch (Exception e)
        {
        }
        if (defaultchoice == -1)
        {
            return askYesNoCancel(question, defaultchoice);
        }
        return defaultchoice;
    }

    private String read() throws Exception
    {
        byte[] byteArray = {(byte) System.in.read()};
        return new String(byteArray);
    }

    private String readln() throws Exception
    {
        String input = read();
        int available = System.in.available();
        if (available > 0)
        {
            byte[] byteArray = new byte[available];
            System.in.read(byteArray);
            input += new String(byteArray);
        }
        return input.trim();
    }

    /**
     * The destroyer starts.
     *
     * @param name The name of the overall action. Not used here.
     * @param max  The maximum value of the progress.
     */
    public void startAction(final String name, final int max)
    {
        out("Processing " + name);
    }

    /**
     * The destroyer stops.
     */
    public void stopAction()
    {
        out(messages.get("InstallPanel.finished"));
    }

    /**
     * The destroyer progresses.
     *
     * @param pos     The actual position.
     * @param message The message.
     */
    public void progress(final int pos, final String message)
    {
        out(message);
    }

    public void nextStep(String step_name, int step_no, int no_of_substeps)
    {
        // not used
    }

    public void setSubStepNo(int no_of_substeps)
    {
        // not used
    }

    /**
     * Output a notification.
     * <p/>
     * Does nothing here.
     *
     * @param text
     */
    public void emitNotification(String text)
    {
    }

    /**
     * Output a warning.
     *
     * @param text
     */
    public boolean emitWarning(String title, String text)
    {
        return askOKCancel(title + ": " + text, AUTO_ANSWER_MODE);
    }

    /**
     * The destroyer encountered an error.
     *
     * @param error The error message.
     */
    public void emitError(String title, String error)
    {
        out(title + ": " + error);
    }

    /**
     * The destroyer encountered an error.
     *
     * @param error The error message.
     */
    public void emitErrorAndBlockNext(String title, String error)
    {
        emitError(title, error);
    }

    /**
     * Ask the user a question.
     *
     * @param title    Message title.
     * @param question The question.
     * @param choices  The set of choices to present.
     * @return The user's choice.
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#askQuestion(String, String, int)
     */
    public int askQuestion(String title, String question, int choices)
    {
        return askQuestion(title, question, choices, AUTO_ANSWER_MODE);
    }

    /**
     * Ask the user a question.
     *
     * @param title          Message title.
     * @param question       The question.
     * @param choices        The set of choices to present.
     * @param default_choice The default choice. (-1 = no default choice)
     * @return The user's choice.
     * @see com.izforge.izpack.api.handler.AbstractUIHandler#askQuestion(String, String, int, int)
     */
    public int askQuestion(String title, String question, int choices, int default_choice)
    {
        int choice = 0;

        if (choices == AbstractUIHandler.CHOICES_YES_NO)
        {
            choice = askYesNo(title + ": " + question, default_choice);
        }
        else if (choices == AbstractUIHandler.CHOICES_YES_NO_CANCEL)
        {
            choice = askYesNoCancel(title + ": " + question, default_choice);
        }

        return choice;
    }
}
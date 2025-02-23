/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package uk.ac.leeds.ccg.r2d;

import uk.ac.leeds.ccg.r2d.d.RenderImageDouble;

/**
 *
 * @author Andy Turner
 */
public class Run {

    public static void main(String[] args) {
        System.out.println("Argument count: " + args.length);
        for (int i = 0; i < args.length; i++) {
            System.out.println("Argument " + i + ": " + args[i]);
        }
        if (args[0].equalsIgnoreCase("BR")) {
            RenderImage.main(args);
        } else if (args[0].equalsIgnoreCase("D")) {
            RenderImageDouble.main(args);
        } else {
            System.err.println("Argument 0 should be either \"br\" or \"d\"");
            System.exit(1);
        }
    }
}

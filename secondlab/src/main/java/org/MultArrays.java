package org;

import java.io.PrintStream;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.concurrent.ExecutionException;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public class MultArrays {
    private static final String KERNEL = "kernel void mul_arrays(global const float *a, global const float *b, global float *answer) {unsigned int xid = get_global_id(0); answer[xid] = a[xid] * b[xid]; }";
    private static final float[] LEFT_ARRAY = new float[]{1.0F, 3.0F, 5.0F, 7.0F};
    private static final float[] RIGHT_ARRAY = new float[]{2.0F, 4.0F, 6.0F, 8.0F};

    public MultArrays() {
    }

    private static void printSequence(String label, FloatBuffer sequence, PrintStream to) {
        to.print(label);
        to.print(": [ ");

        for(int i = 0; i < sequence.limit(); ++i) {
            to.print(' ');
            to.print(Float.toString(sequence.get(i)));
            to.print(' ');
        }

        to.println(" ]");
    }

    public static void main(String[] args) {
        CLRuntime cl = new CLRuntime();

        try {
            MemoryStack stack = MemoryStack.stackPush();

            try {
                CLRuntime.Platform platform = (CLRuntime.Platform)cl.getPlatforms().first();
                CLRuntime.Device device = platform.getDefault();

                try {
                    CLRuntime.Context context = device.createContext();

                    try {
                        CLRuntime.Program program = context.createProgramWithSource("kernel void mul_arrays(global const float *a, global const float *b, global float *answer) {unsigned int xid = get_global_id(0); answer[xid] = a[xid] * b[xid]; }");

                        try {
                            FloatBuffer lhs = stack.floats(LEFT_ARRAY);
                            FloatBuffer rhs = stack.floats(RIGHT_ARRAY);
                            printSequence("Left hand statement: ", lhs, System.out);
                            printSequence("Right hand statement: ", rhs, System.out);
                            int gws = LEFT_ARRAY.length * 4;
                            CLRuntime.CommandQueue cq = program.getCommandQueue();
                            CLRuntime.VideoMemBuffer first = cq.hostPtrReadBuffer(MemoryUtil.memAddressSafe(lhs), gws);
                            CLRuntime.VideoMemBuffer second = cq.hostPtrReadBuffer(MemoryUtil.memAddressSafe(rhs), gws);
                            CLRuntime.VideoMemBuffer answer = cq.createReadWriteBuffer(gws);
                            cq.flush();
                            CLRuntime.Kernel sumVectors = program.createKernel("mul_arrays");
                            sumVectors.arg(first).arg(second).arg(answer).executeAsDataParallel((long)gws);
                            ByteBuffer result = MemoryUtil.memAlloc(answer.getCapacity());
                            cq.readVideoMemory(answer, result);
                            printSequence("Result: ", result.asFloatBuffer(), System.out);
                        } catch (Throwable var20) {
                            if (program != null) {
                                try {
                                    program.close();
                                } catch (Throwable var19) {
                                    var20.addSuppressed(var19);
                                }
                            }

                            throw var20;
                        }

                        if (program != null) {
                            program.close();
                        }
                    } catch (Throwable var21) {
                        if (context != null) {
                            try {
                                context.close();
                            } catch (Throwable var18) {
                                var21.addSuppressed(var18);
                            }
                        }

                        throw var21;
                    }

                    if (context != null) {
                        context.close();
                    }
                } catch (ExecutionException var22) {
                    System.err.println(var22.getMessage());
                    System.exit(-1);
                }
            } catch (Throwable var23) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var17) {
                        var23.addSuppressed(var17);
                    }
                }

                throw var23;
            }

            if (stack != null) {
                stack.close();
            }
        } catch (Throwable var24) {
            try {
                cl.close();
            } catch (Throwable var16) {
                var24.addSuppressed(var16);
            }

            throw var24;
        }

        cl.close();
    }
}

/** Copyright © 2021-2050 刘路峰版权所有。 */
package com.easecurity.util;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.CompilationMXBean;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryManagerMXBean;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryUsage;
import java.lang.management.OperatingSystemMXBean;
import java.lang.management.RuntimeMXBean;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 与MBean及系统有关的工具类
 *
 */
public class MBeanUtils {

    /**
     * Java 虚拟机的运行时系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, String> showJvmInfo(StringBuffer outString) {
        Map<String, String> result = new HashMap<>();
        RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
        result.put("jvm_name", mxbean.getName());
        if (outString != null)
            outString.append("\njvm_name:").append(mxbean.getName());

        result.put("jvm_vmName", mxbean.getVmName());
        if (outString != null)
            outString.append("\njvm_vmName:").append(mxbean.getVmName());

        result.put("jvm_version", mxbean.getVmVersion());
        if (outString != null)
            outString.append("\njvm_version:").append(mxbean.getVmVersion());

        try {
            result.put("jvm_bootClassPath", mxbean.getBootClassPath());
            if (outString != null)
                outString.append("\njvm_bootClassPath:").append(mxbean.getBootClassPath());
        } catch (Exception e) {
            // 在JDK 9 及更高版本中，由于引入了Java平台模块系统（JPMS），RuntimeMXBean.getBootClassPath() 方法已被移除。
            String classPath = System.getProperty("java.class.path");
            result.put("jvm_bootClassPath", classPath);
            if (outString != null)
                outString.append("\njvm_bootClassPath:").append(classPath);
        }

        result.put("jvm_startTime", String.valueOf(mxbean.getStartTime()));
        if (outString != null)
            outString.append("\njvm_startTime:").append(mxbean.getStartTime());

        return result;
    }

    /**
     * Java 虚拟机的内存系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, String> showMemoryInfo(StringBuffer outString) {
        Map<String, String> result = new HashMap<>();
        MemoryMXBean mem = ManagementFactory.getMemoryMXBean();
        MemoryUsage heap = mem.getHeapMemoryUsage();
        result.put("Heap_committed", String.valueOf(heap.getCommitted()));
        if (outString != null)
            outString.append("\nHeap_committed:").append(heap.getCommitted());

        result.put("Heap_init", String.valueOf(heap.getInit()));
        if (outString != null)
            outString.append("\nHeap_init:").append(heap.getInit());

        result.put("Heap_max", String.valueOf(heap.getMax()));
        if (outString != null)
            outString.append("\nHeap_max:").append(heap.getMax());

        result.put("Heap_used", String.valueOf(heap.getUsed()));
        if (outString != null)
            outString.append("\nHeap_used:").append(heap.getUsed());

        return result;
    }

    /**
     * Java 虚拟机在其上运行的操作系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, String> showSystem(StringBuffer outString) {
        Map<String, String> result = new HashMap<>();
        OperatingSystemMXBean op = ManagementFactory.getOperatingSystemMXBean();
        result.put("System_architecture", String.valueOf(op.getArch()));
        if (outString != null)
            outString.append("\nSystem_architecture:").append(op.getArch());

        result.put("System_processors", String.valueOf(op.getAvailableProcessors()));
        if (outString != null)
            outString.append("\nSystem_processors:").append(op.getAvailableProcessors());

        result.put("System_name", String.valueOf(op.getName()));
        if (outString != null)
            outString.append("\nSystem_name:").append(op.getName());

        result.put("System_version", String.valueOf(op.getVersion()));
        if (outString != null)
            outString.append("\nSystem_version:").append(op.getVersion());

        result.put("System_load", String.valueOf(op.getSystemLoadAverage()));
        if (outString != null)
            outString.append("\nSystem_load:").append(op.getSystemLoadAverage());

        return result;
    }

    /**
     * Java 虚拟机的类加载系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, String> showClassLoading(StringBuffer outString) {
        Map<String, String> result = new HashMap<>();
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        result.put("ClassLoading_totalLoadedClassCount", String.valueOf(cl.getTotalLoadedClassCount()));
        if (outString != null)
            outString.append("\nClassLoading_totalLoadedClassCount:").append(cl.getTotalLoadedClassCount());

        result.put("ClassLoading_loadedClassCount", String.valueOf(cl.getLoadedClassCount()));
        if (outString != null)
            outString.append("\nClassLoading_loadedClassCount:").append(cl.getLoadedClassCount());

        result.put("ClassLoading_unloadedClassCount", String.valueOf(cl.getUnloadedClassCount()));
        if (outString != null)
            outString.append("\nClassLoading_unloadedClassCount:").append(cl.getUnloadedClassCount());

        return result;
    }

    /**
     * Java 虚拟机的编译系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, String> showCompilation(StringBuffer outString) {
        Map<String, String> result = new HashMap<>();
        CompilationMXBean com = ManagementFactory.getCompilationMXBean();
        result.put("Compilation_name", String.valueOf(com.getName()));
        if (outString != null)
            outString.append("\nCompilation_name:").append(com.getName());

        result.put("Compilation_totalCompilationTime", String.valueOf(com.getTotalCompilationTime()));
        if (outString != null)
            outString.append("\nCompilation_totalCompilationTime:").append(com.getTotalCompilationTime());

        return result;
    }

    /**
     * Java 虚拟机的线程系统信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, Object> showThread(StringBuffer outString) {
        Map<String, Object> result = new HashMap<>();
        ThreadMXBean thread = ManagementFactory.getThreadMXBean();
        result.put("Thread_threadCount", String.valueOf(thread.getThreadCount()));
        if (outString != null)
            outString.append("\nThread_threadCount:").append(thread.getThreadCount());

        result.put("Thread_allThreadIds", String.valueOf(thread.getAllThreadIds()));
        if (outString != null)
            outString.append("\nThread_allThreadIds:").append(thread.getAllThreadIds());

        result.put("Thread_currentThreadUserTime", String.valueOf(thread.getCurrentThreadUserTime()));
        if (outString != null)
            outString.append("\nThread_currentThreadUserTime:").append(thread.getCurrentThreadUserTime());
        // ......还有其他很多信息

        return result;
    }

    /**
     * Java 虚拟机中的垃圾回收器信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, Object> showGarbageCollector(StringBuffer outString) {
        Map<String, Object> result = new HashMap<>();
        List<GarbageCollectorMXBean> gcs = ManagementFactory.getGarbageCollectorMXBeans();
        for (GarbageCollectorMXBean gc : gcs) {
            result.put("GarbageCollector_" + gc.getName(), gc);
            if (outString != null)
                outString.append("\nGarbageCollector_" + gc.getName() + ":").append(gc);

            result.put("GarbageCollector_" + gc.getName() + "_collectionCount", gc.getCollectionCount());
            if (outString != null)
                outString.append("\nGarbageCollector_" + gc.getName() + "_collectionCount:").append(gc.getCollectionCount());

            result.put("GarbageCollector_" + gc.getName() + "_collectionTime", gc.getCollectionTime());
            if (outString != null)
                outString.append("\nGarbageCollector_" + gc.getName() + "_collectionTime:").append(gc.getCollectionTime());
        }

        return result;
    }

    /**
     * Java 虚拟机中的内存管理器信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, Object> showMemoryManager(StringBuffer outString) {
        Map<String, Object> result = new HashMap<>();
        List<MemoryManagerMXBean> mm = ManagementFactory.getMemoryManagerMXBeans();
        for (MemoryManagerMXBean eachmm : mm) {
            result.put("MemoryManager_" + eachmm.getName(), eachmm);
            if (outString != null)
                outString.append("\nMemoryManager_" + eachmm.getName() + ":").append(eachmm);

            result.put("MemoryManager_" + eachmm.getName() + "_memoryPoolNames", eachmm.getMemoryPoolNames());
            if (outString != null)
                outString.append("\nMemoryManager_" + eachmm.getName() + "_memoryPoolNames:").append(eachmm.getMemoryPoolNames());
        }
        return result;
    }

    /**
     * Java 虚拟机中的内存池信息
     * 
     * @param outString 结果信息以String输出
     */
    public static Map<String, Object> showMemoryPool(StringBuffer outString) {
        Map<String, Object> result = new HashMap<>();
        List<MemoryPoolMXBean> mps = ManagementFactory.getMemoryPoolMXBeans();
        for (MemoryPoolMXBean mp : mps) {
            result.put("MemoryPool_" + mp.getName(), mp);
            if (outString != null)
                outString.append("\nMemoryPool_" + mp.getName() + ":").append(mp);

            result.put("MemoryPool_" + mp.getName() + "_collectionUsage", mp.getCollectionUsage());
            if (outString != null)
                outString.append("\nMemoryPool_" + mp.getName() + "_collectionUsage:").append(mp.getCollectionUsage());

            result.put("MemoryPool_" + mp.getName() + "_type", mp.getType());
            if (outString != null)
                outString.append("\nMemoryPool_" + mp.getName() + "_type:").append(mp.getType());
        }
        return result;
    }

    /**
     * 获取MXBean
     * 
     */
    public static RuntimeMXBean visitMBean() {
        // 第一种直接调用同一 Java 虚拟机内的 MXBean 中的方法
        RuntimeMXBean mxbean = ManagementFactory.getRuntimeMXBean();
//	String vendor1 = mxbean.getVmVendor();
//	System.out.println("vendor1:" + vendor1);
        return mxbean;

//        //第二种通过一个连接到正在运行的虚拟机的平台 MBeanServer 的 MBeanServerConnection
//        MBeanServerConnection mbs = null;
//        try {
//            // Assuming the RuntimeMXBean has been registered in mbs
//            ObjectName oname = new ObjectName(ManagementFactory.RUNTIME_MXBEAN_NAME);
//            String vendor2 = (String) mbs.getAttribute(oname, "VmVendor");
//            System.out.println("vendor2:" + vendor2);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        
//        //第三种使用 MXBean 代理
//        MBeanServerConnection mbs3 = null;
//        RuntimeMXBean proxy;
//        try {
//            proxy = ManagementFactory.newPlatformMXBeanProxy(mbs3,ManagementFactory.RUNTIME_MXBEAN_NAME, RuntimeMXBean.class);
//            String vendor = proxy.getVmVendor();
//            System.out.println("vendor3:" + vendor);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        StringBuffer outString = new StringBuffer();
        showJvmInfo(outString);
        showMemoryInfo(outString);
        showSystem(outString);
        showClassLoading(outString);
        showCompilation(outString);
        showThread(outString);
        showGarbageCollector(outString);
        showMemoryManager(outString);
        showMemoryPool(outString);
        visitMBean();
        System.out.println(outString.toString().trim());
    }
}

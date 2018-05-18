package de.dytanic.cloudnet;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParser;
import com.google.gson.internal.bind.TypeAdapters;
import de.dytanic.cloudnet.document.Document;
import de.dytanic.cloudnet.document.gson.DocumentTypeAdapter;
import de.dytanic.cloudnet.logging.ILogProvider;
import de.dytanic.cloudnet.scheduler.TaskScheduler;
import de.dytanic.cloudnet.util.Properties;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.util.*;
import java.util.concurrent.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public final class $ {

    private $()
    {
    }

    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapterFactory(TypeAdapters.newTypeHierarchyFactory(Document.class, new DocumentTypeAdapter()))
            .create();

    public static final JsonParser JSON_PARSER = new JsonParser();

    public static final File CURRENT_DIRECTORY = new File("");

    public static final String

            LINE_SEPARATOR = System.lineSeparator(),
            SYS_PROPERTY_USER = System.getProperty("user.name"),
            SYS_PROPERTY_OS_NAME = System.getProperty("os.name"),
            SYS_PROPERTY_OS_VERSION = System.getProperty("os.version"),
            EMPTY_STRING = "",
            SPACE_STRING = " ",
            SLASH_STRING = "/",
            DOT_STRING = "\\.";

    @Getter
    @Setter
    private static ILogProvider logger;

    @Getter
    @Setter
    private static TaskScheduler scheduler;

    /*= ------------------------------------------------------------------------- =*/

    public static Properties parseLine(String line)
    {
        if (line.trim().isEmpty()) return null;

        return parseLine(line.split(SPACE_STRING));
    }

    public static Properties parseLine(String[] args)
    {
        Properties properties = new Properties();

        for (String argument : args)
        {
            if (argument.isEmpty() || argument.equals($.EMPTY_STRING) || argument.equals($.SPACE_STRING)) continue;

            if (argument.contains("="))
            {
                int x = argument.indexOf("=");
                properties.put(argument.substring(0, x).replaceFirst("-", EMPTY_STRING).replaceFirst("-", EMPTY_STRING), argument.substring(x + 1, argument.length()));
                continue;
            }

            if (argument.contains("--") || argument.contains("-"))
            {
                properties.put(argument.replaceFirst("-", EMPTY_STRING).replaceFirst("-", EMPTY_STRING), "true");
                continue;
            }

            properties.put(argument, "true");
        }

        return properties;
    }

    public static boolean isNumber(String input)
    {
        try
        {
            Double.parseDouble(input);
            return true;
        } catch (NumberFormatException ex)
        {
            return false;
        }
    }

    public static String randomString(int size)
    {
        char[] charSet = "abcdefghijklmnopqrstuvwxyz0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".toCharArray();
        Random random = new Random();

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i++ < size; stringBuilder.append(charSet[random.nextInt(charSet.length)])) ;

        return stringBuilder.toString();
    }

    /*= ------------------------------------------------------------------------- =*/

    public static <T> T filterFirst(Iterable<T> iterable, Predicate<T> predicate)
    {
        for (T entry : iterable) if (predicate.test(entry)) return entry;

        return null;
    }

    public static <T> T filterFirst(T[] array, Predicate<T> predicate)
    {
        for (T entry : array) if (predicate.test(entry)) return entry;

        return null;
    }

    public static <T> Collection<T> filter(T[] array, Predicate<T> predicate)
    {
        Collection<T> collection = newLinkedList();

        for (T entry : array) if (predicate.test(entry)) collection.add(entry);

        return collection;
    }

    public static <T> Collection<T> filter(Iterable<T> iterable, Predicate<T> predicate)
    {
        Collection<T> collection = newLinkedList();

        for (T entry : iterable) if (predicate.test(entry)) collection.add(entry);

        return collection;
    }

    public static <T, E> Collection<E> transform(T[] array, Function<T, E> function)
    {
        Collection<E> collection = newArrayList();

        for (T entry : array) collection.add(function.apply(entry));

        return collection;
    }

    public static <T, E> Collection<E> transform(Iterable<T> entries, Function<T, E> function)
    {
        Collection<E> collection = newArrayList();

        for (T entry : entries) collection.add(function.apply(entry));
        return collection;
    }

    public static <E> void forEach(Enumeration<E> enumeration, Consumer<E> consumer)
    {
        if (enumeration == null || consumer == null) return;

        while (enumeration.hasMoreElements()) consumer.accept(enumeration.nextElement());
    }

    public static <E> void forEach(Iterator<E> iterator, Consumer<E> consumer)
    {
        if(iterator == null || consumer == null) return;

        while (iterator.hasNext()) consumer.accept(iterator.next());
    }

    public static <K, V> void addAll(Map<K, V> map, Collection<V> values, Function<V, K> function)
    {
        if (values == null || map == null || function == null) return;

        for (V value : values) map.put(function.apply(value), value);
    }

    /*= ------------------------------------------------------------------ =*/

    public static void sleepUninterruptedly(long millis)
    {
        sleepUninterruptedly(millis, 0);
    }

    public static void sleepUninterruptedly(long millis, int nanos)
    {
        try
        {
            Thread.sleep(millis, nanos);
        } catch (InterruptedException e)
        {
        }
    }

    public static String build(String[] array, String split)
    {
        StringBuilder stringBuilder = new StringBuilder();

        for (String entry : array) stringBuilder.append(entry).append(split);

        return replaceLast(stringBuilder.toString(), split, "");
    }

    public static String replaceLast(String string, String toReplace, String replacement)
    {
        int pos = string.lastIndexOf(toReplace);
        if (pos > -1)
            return string.substring(0, pos) + replacement + string.substring(pos + toReplace.length(), string.length());
        else
            return string;
    }

    /*= ------------------------------------------------------------------------- =*/

    public static Properties newProperties()
    {
        return new Properties();
    }

    public static <T> CopyOnWriteArrayList<T> newCopyOnWriteArrayList()
    {
        return new CopyOnWriteArrayList<>();
    }

    public static <T> ArrayList<T> newArrayList()
    {
        return new ArrayList<>();
    }

    public static <T> ArrayList<T> newArrayList(Iterable<T> iterable)
    {
        ArrayList<T> arrayList = newArrayList();

        for (T entry : iterable) arrayList.add(entry);

        return arrayList;
    }

    public static <T> LinkedList<T> newLinkedList()
    {
        return new LinkedList<>();
    }

    public static <T> ConcurrentLinkedDeque<T> newConcurrentLinkedDeque()
    {
        return new ConcurrentLinkedDeque<>();
    }

    public static <T> LinkedBlockingDeque<T> newLinkedBlockingDeque()
    {
        return new LinkedBlockingDeque<>();
    }

    public static <T> HashSet<T> newHashSet()
    {
        return new HashSet<>();
    }

    public static <T> ConcurrentLinkedQueue<T> newConcurrentLinkedQueue()
    {
        return new ConcurrentLinkedQueue<>();
    }

    public static <T> LinkedBlockingQueue<T> newBlockingQueue()
    {
        return new LinkedBlockingQueue<>();
    }

    public static <T> ConcurrentSkipListSet<T> newConcurrentSkipListSet()
    {
        return new ConcurrentSkipListSet<>();
    }

    public static <T> CopyOnWriteArraySet<T> newCopyOnWriteArraySet()
    {
        return new CopyOnWriteArraySet<>();
    }

    public static <K, V> TreeMap<K, V> newTreeMap()
    {
        return new TreeMap<>();
    }

    public static <K, V> HashMap<K, V> newHashMap()
    {
        return new HashMap<>();
    }

    public static <K, V> ConcurrentHashMap<K, V> newConcurrentHashMap()
    {
        return new ConcurrentHashMap<>();
    }

    public static <K, V> LinkedHashMap<K, V> newLinkedMap()
    {
        return new LinkedHashMap<>();
    }

    public static <K, V> IdentityHashMap<K, V> newIdentityHashMap()
    {
        return new IdentityHashMap<>();
    }

    public static Document newEmptyDocument()
    {
        return new Document();
    }

}
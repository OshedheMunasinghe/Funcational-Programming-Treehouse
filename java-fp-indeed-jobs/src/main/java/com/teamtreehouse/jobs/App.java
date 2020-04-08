package com.teamtreehouse.jobs;

import com.teamtreehouse.jobs.model.Job;
import com.teamtreehouse.jobs.service.JobService;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class App {

    public static void main(String[] args) {
        JobService service = new JobService();
        boolean shouldRefresh = false;
        try {
            if (shouldRefresh) {
                service.refresh();
            }
            List<Job> jobs = service.loadJobs();
            System.out.printf("Total jobs:  %d %n %n", jobs.size());
            explore(jobs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void explore(List<Job> jobs) {
        // Your amazing code below...
        // printPortlandJobsImperatively(jobs);
        //  printPortlandsJobsStream(jobs);
        //  getThreeJuniorJobsImperatively(jobs).forEach(System.out::println);
        //  getThreeJuniorJobsStream(jobs).forEach(System.out::println);
        // getCaptionsImperatively(jobs).forEach(System.out::println);
        // getCaptionsStream(jobs).forEach(System.out::println);
        //  getSnippetWordCountsImperatively(jobs).forEach((key, value) -> System.out.printf("'%s' occurs %d times %n", key, value));

     /*   System.out.println(jobs.stream()
                .map(Job::getCompany)
                .mapToInt(String::length)
                .max());
                // Denna returnerar OptionalInt 71
      */
        //  System.out.println(jobs.stream().map(Job::getCompany).max(Comparator.comparingInt(String::length))); //a company who gets a longest name


        //Sexier by using higher order function. By calling Comparator to compare two objects
        //  System.out.println(jobs.stream().map(Job::getCompany).max(Comparator.comparingInt(String::length)));
/** För att unvika få Nullexpsion likande kan Optional ghjälpa till. Men nnackdelen är att du vet själv inte
 * om en object har ingenting då måste du skriva orElse för att varna till user ...**/
    /*
        String searchTerm = "Java";
        Optional<Job> foundJob = luckySearchJob(jobs, searchTerm);
        System.out.println(foundJob
                .map(Job::getTitle)
                .orElse("No Jobs Found"));

     */

        /** RANGE: för att skriva menu varje nummer i varje företag**/
      /*  List<String> companies = jobs.stream()
                .map(Job::getCompany)
                .distinct()
                .sorted()
                .collect(Collectors.toList());

       */


        //displayCompaniesMenuImperatively(companies); //callar noob method
        //  displayCompaniesMenuUsingRange(companies);

/*
        //count ever 21 pages by using Iterate
        int pageSize = 20;
        int numPages = companies.size() / pageSize;

        IntStream.iterate(1, i-> i + pageSize)
                .mapToObj(i -> String.format("%d. %s" , i , companies.get(i)))
                .limit(numPages)
                .forEach(System.out::println);


 */

        /** SIDE EFFECT
         * by helping peek instead calling system.out helps to find out where the bug is
         * sometimes you can not trust forEach loop**/
        /* companies.stream()
                .peek(company -> System.out.println("===>" + company))
                .filter(company -> company.startsWith("N"))
                .forEach(System.out::println);

         */


        //This is a side effect
     /*   Job firstOne = jobs.get(0);
        System.out.println("First job: " + firstOne);
        Predicate<Job> caJobChecker = job -> job.getState().equals("CA");

        //This is cleaner non side effects
        Job caJob = jobs.stream()
                .filter(caJobChecker)
                .findFirst()
                .orElseThrow(NullPointerException::new);

        emailIfMatches(firstOne,caJobChecker);
       // emailIfMatches(caJob,caJobChecker);  // returns: I am sending an email about Job{title='Java Developer', company='ProMedia Careers', city='San Diego', state='CA', country='US'}
        emailIfMatches(caJob, caJobChecker.and(App::isJuniorJob)); // this is higher order function

      */

/*
        Function<String, LocalDateTime> indeedDateConverter =
                dateString -> LocalDateTime.parse(
                        dateString,
                        DateTimeFormatter.RFC_1123_DATE_TIME
                );

        Function<LocalDateTime,String> siteDateStringConverter =
                date -> date.format((DateTimeFormatter.ofPattern("M / d / YY")));



        jobs.stream()
                .map(Job::getDateTimeString)
                .map(indeedDateConverter.andThen(siteDateStringConverter)) //Function Composition function -> function , man kan bryta ut denna rad och kalla en variable för att koden ska se snyggt ut
                .limit(5)
                .forEach(System.out::println);
 */


        //About Closure Function by calling CreateDateStringConveter

        Function<String,String> converter = createDateStringConverter(DateTimeFormatter.RFC_1123_DATE_TIME , DateTimeFormatter.ISO_DATE);

        jobs.stream()
                .map(Job::getDateTimeString)
                .map(converter) //Function Composition function -> function , man kan bryta ut denna rad och kalla en variable för att koden ska se snyggt ut
                .limit(5)
                .forEach(System.out::println);

    }//end main

    //Training for Closure
    public static Function<String,String> createDateStringConverter(DateTimeFormatter inFormatter, DateTimeFormatter outForMatter){
        return dateString -> LocalDateTime.parse(dateString,inFormatter).format(outForMatter); // där inne parse(....) dessa två scopes callas closure.
        // När det finns dubbel return kallas closure
    }

    private static void emailIfMatches(Job job, Predicate<Job> checker) {
        if (checker.test(job)) {
            System.out.println("I am sending an email about " + job);
        }
    }//end emailIfMatches

    //i lamda professional way
    private static void displayCompaniesMenuUsingRange(List<String> companies) {
        IntStream.rangeClosed(1, 20)
                .mapToObj(i -> String.format("%d. %s", i, companies.get(i - 1)))
                .forEach(System.out::println);
    }//end displayCompaniesMenuUsingRange

    //This is how the classical way of standard noob programming
    private static void displayCompaniesMenuImperatively(List<String> companies) {
        for (int i = 0; i < 20; i++) {
            System.out.printf("%d. %s %n", i + 1, companies.get(i));
        }//end for
    }//end displayCompaniesMenuImperatively

    private static Optional<Job> luckySearchJob(List<Job> jobs, String searchTerm) {
        return jobs.stream()
                .filter(job -> job.getTitle().contains(searchTerm))
                .findFirst();
    }


    /**
     * Uppdrag är att fånga hel text och platta till det. För att texten ligger i lista i en lista. Stream classen kan åtgärda det.
     **/
    public static Map<String, Long> getSnippetWordCountsStream(List<Job> jobs) {
        return jobs.stream()
                .map(Job::getSnippet)
                .map(snippet -> snippet.split("\\W+"))
                .flatMap(words -> Stream.of(words))
                .filter(word -> word.length() > 0)
                .map(String::toLowerCase)
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    }//end getSnippetWordCountsStream

    /**
     * Jag har lagt varje rad kod i Lamda översättning
     **/
    public static Map<String, Long> getSnippetWordCountsImperatively(List<Job> jobs) {
        Map<String, Long> wordCounts = new HashMap<>();

        for (Job job : jobs) {
            String[] words = job.getSnippet().split("\\W+"); //   .map(snippet -> snippet.split("\\W+")) .flatMap(words -> Stream.of(words))
            for (String word : words) { // Plattar till texten:  .flatMap(words -> Stream.of(words))
                if (word.length() == 0) { // .filter(word -> word.length() > 0)
                    continue;
                }
                String lWord = word.toLowerCase(); // .map(String::toLowerCase)
                Long count = wordCounts.get(lWord);
                if (count == null) {
                    count = 0L;
                }
                wordCounts.put(lWord, ++count); ////  .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
            }
        }
        return wordCounts;
    }// end getSnippetWordCountsImperatively

    // this time we have to call map
    private static List<String> getCaptionsStream(List<Job> jobs) {
        return jobs.stream()
                .filter(App::isJuniorJob) //isJuniorJob är static och den har parameter
                .map(Job::getCaption) //Denna kallar från klassen Job dock getCaption har ingen parameter. Instans method funkar.
                .limit(3).collect(Collectors.toList());
    }

    private static List<String> getCaptionsImperatively(List<Job> jobs) {
        List<String> captions = new ArrayList<>();
        for (Job job : jobs) {
            if (isJuniorJob(job)) {
                captions.add(job.getCaption());
                if (captions.size() >= 3) { // he wants to pick max 3 junior jobs
                    break;
                }// end inner if
            }//end if
        }//end for
        return captions;
    }


    private static List<Job> getThreeJuniorJobsStream(List<Job> jobs) {
        return jobs.stream()
                .filter(App::isJuniorJob).limit(3).collect(Collectors.toList()); //we need to call Collect to return a list of jobs otherwise it calls error


        /** THIS IS ILLIGAL WAY
         * Because for each requires outside the stream, it is bad form, side effects**/
     /*   List<Job> juniorJob = new ArrayList<>();
         jobs.stream()
                .filter(App::isJuniorJob).limit(3).forEach(juniorJob::add);
         return juniorJob;

      */
    }//end getThreeJuniorJobsStream

    private static boolean isJuniorJob(Job job) {
        String title = job.getTitle().toLowerCase();
        return (title.contains("junion") || title.contains("jr"));
    }//end isJuniorJob

    private static List<Job> getThreeJuniorJobsImperatively(List<Job> jobs) {
        List<Job> juniorJob = new ArrayList<>(); //Once we have got some junior jobs it will add in a new list.
        for (Job job : jobs) {
            if (isJuniorJob(job)) {
                juniorJob.add(job);
                if (juniorJob.size() >= 3) { // he wants to pick max 3 junior jobs
                    break;
                }// end inner if
            }//end if
        }//end for
        return juniorJob;
    }//end getThreeJuniorJobsImperatively

    private static void printPortlandsJobsStream(List<Job> jobs) {
        jobs.stream().filter(job -> job.getState().equals("OR"))
                .filter(job -> job.getCity().equals("Portland"))
                .forEach(System.out::println);
    }

    private static void printPortlandJobsImperatively(List<Job> jobs) {
        for (Job job : jobs) {
            if (job.getState().equals("OR") && job.getCity().equals("Portland")) {
                System.out.println(job);
            }
        }//end for
    }
}

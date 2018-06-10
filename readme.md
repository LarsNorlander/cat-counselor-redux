# CAT Counselor Redux: A 100 Days of Code Project

This is a rewrite of the application for my college thesis, CAT Counselor. CAT Counselor used to compose of two parts,
a [CAT Ranker](https://github.com/LarsNorlander/cat-ranker), which was just a REST API that took in the data needed by
the main system – [CAT Counselor](https://github.com/LarsNorlander/cat-counselor), and spat out the complete results.

The reason for this split was just so that I could finish my thesis as quickly as possible. Ranker was written in Groovy
and Spring because I was most comfortable with Groovy writing algorithms and it was the language I was taught to use
during my summer job at TORO. Counselor on the other hand was written in PHP and Laravel because that was the language
and framework I was very well versed with for writing systems before my summer job.

> You could view my original thesis [here](https://drive.google.com/drive/folders/0Bw9dayd0bBAGbUs4OVU2VWppdlU?usp=sharing).
I know, it's pretty rough and definitely could have been a lot better.

## Why a Rewrite?

Because the old code is abysmal! I have no idea what's going on in there now that I look at it. Luckily, I remember all
the requirements of the system since I designed it myself (and wrote it just less than two years back). Since the time
that I've wrote that thing, I've learned so many things and have read so many books. So I'd like to apply the things 
I've learned like Clean Code, Test Driven Development, Domain Driven Design (well, still learning that, I'll try), 
the SOLID principles (the actual proper SOLID principles) and a bunch more. I'm also writing this in Kotlin. Why Kotlin?
Well, coz why not? Actually no, I want to learn the language and apply it properly.

## 100 Days of Code

I've been putting this project off for a while now and the [#100DaysOfCode](http://www.100daysofcode.com/) challenge.
I've finally decided to bring the two together so that I could hit two birds with one stone. Coding an hour a day is
definitely better than coding zero hours per day as I've been doing previously.

## A Bunch of Random 💩

*This section is going to get really freakin' messy. It's just going to be a huge brain dump here on out.*

### The Why of CAT Counselor

CAT Counselor is actually short for Computerized Academic Track Counselor. Shortening it to CAT Counselor just gave me a
cheap mascot. So for context, here in the Philippines K-12 is divided into four levels of preschool, six grades of 
elementary, four years of junior high school, and two year of senior high school. In senior high school, there are 
several tracks students could choose from. However, if you plan on going to college, you'll have to take up the Academic
Track. This Academic Track is further divided into four *strands*, namely:

* Accountancy, Business and Management (ABM)
* Humanities and Social Sciences (HUMSS)
* Science, Technology, Engineering and Mathematics (STEM)
* General Academic Strand (GAS)

Most students however have no idea what they want in their career life. This is evidenced by the fact that in our
university, students keep shifting courses. 

> Now, I didn't really measure this properly for my thesis back then but our professor probably let it pass at the time 
because she knew it was true. I realize now that this is a big flaw in my study.

This shifting of courses would be more problematic for the new generation of students who have to pick a strand in 
junior high school. If they pick ABM and decide it's not for them when they reach college, what then? That's a pretty 
expensive mistake – people are already complaining about the addition of two years.

So with that, CAT Counselor is aimed at helping students determine which strand is best for them given their grades,
career assessment exam, awards and preferences. The algorithm of course, is a heuristic. We developed it with the help 
of educators in our area and we found that most students got a better understanding of what's best for them and what 
they have to improve if their best preference is not at the top of the results.

### How The Algorithm Works

When we talked to the educator about how we'd help students, we basically associate certain subjects to a strand. 
Meaning if a student is good at science and math, they'll probably fit well in STEM. But if say, STEM required more than
that but another strand required less, they'd probably be better off in that other stand. So, how do we compute this?

Well, we take all the student's grades and average it. This average is then used to get the student's set of strengths 
by taking all the subjects that have a grade higher than it. Each strand then has a set of required subjects that is
determined by the educator. First, we get the union of both sets and rank strands by number of elements in the union. 
The greater the number, the more a strand is recommended. If there's a tie however, we then get the difference of the
set of requirements and the set of strengths. This will give us a set containing all the subjects required by the strand
that the student isn't strong in. We then break the tie by comparing the size of this set, the smaller the number of 
unmet requirements, the more it would be recommended. Finally, if there's still a tie, the one the student prefers is 
going to rank higher.

This computation works the same way for other measurements like the career assessment exam and number of awards. The
results of these different criteria are then averaged. There is a problem however with the way this was originally 
implemented. Originally, each criteria was sorted using the method discussed in the previous paragraph and then given
scores. These scores were then aggregated from the different criteria to create the final ranking and if there was a 
tie, it would be broken by preference. This created a situation where one strand should have been more suited to
a student when you look at the detailed results. The mistake here is that preference should only be used as a final tie
breaker when all the scores have been aggregated and shouldn't have been used at the criteria level.

### Building Up The Project

I used to write apps in a way that just yells, "WEB APP!!!" to your face. All the implementation details like databases 
and presentation were front and center. I now know that it's wrong (if you follow the school of 
[Uncle Bob](https://github.com/unclebob)) to do so and that the architecture of the system should say, "Counseling App!"
instead.

> But despite the fact, I have generated the project using Spring Initializr. It's just convenient and please give me a 
pass on this one.

This time around, I'll do my best to develop the app with use cases and push implementation details till the
last minute, strive to do test driven development, and properly model my domain.

Well... I'll try at least. Here I go.

---
# Brain Dumps 💩

## June 4, 2018

The top most important thing is the Strand, not the criterion.

So basically, it should be something like,

For the Science, Technology, Engineering and Math strand, you’ll need to be good at Science and Math. In the NCAE side 
of things, you’ll need to have Logical Ability, Scientific Thinking and something else.

So that should look like this:

```yaml
STEM:
    Grades:
        - Science
        - Math
    NCAE:
        - Logical Ability
        - Scientific Thinking
GAS:
    Grades:
        - English
        - Filipino
```

Getting the list of strands should then be as easy as getting all the keys. Getting all the fields for a criterion would
 be, going through the values, and getting the union of each set.

and so the Specification class should have the following properties

```
Specification                     

+ strands: List<Strand>            
+ criteria: Set<Criteria>          
+ itemsForCriterion: Set<Item>     
```

The strands should contain the following fields

```
Strand

+ name: String
+ requirements: Map<Criterion, Set<Item>>
```

Type Aliases
```kotlin
typealias Criteria = String
typealias Item = String
```

Given a specification, the counselor should be able to do a few things.

Use cases:
* Tell a student what their statistics are for a particular strand
    * Tell the student the strand they are asking does not exist›
* Score the different strands for a criteria, e.g. score the strands given just grades
* Score the different strands for all available data

```
Counselor

+ computeStatistics(strandName: String, records:Map<Criterion, Map<Item, Score>>): StrandStatistics
```

## June 6, 2018

Now that I have a counselor that's tested for the things I know it should be doing for time, what's next? I'm thinking
of adding a REST API. Testing REST APIs is something I haven't really done, but to make things simple, the logic that's
going to go into validation for the REST API should fall into it's own object or function that could be tested in 
isolation. That way, testing the REST APIs won't be as problematic.

The REST APIs will have to take in data but, how hard should validation be? Let's save that for after work since the
workday is about to start. 

## June 7, 2018

Instead of going for a REST API first, I think I'll be doing a UI instead using thymeleaf. For this, it could be a 
simple form that shows all the fields that matter, taken from the specification so that students could fill up their 
data. So for that, I'll have to create a method that returns a Map of Criteria and all it's significant fields (that's
totally a made up term for now.)

## June 10, 2018

So! Turns out IntelliJ can't resolve the attributes for Thymeleaf when using Kotlin for the Controller. Which is, kinda
a shame. But I've already reported it to them [here](https://youtrack.jetbrains.com/issue/KT-24830). So instead, I've 
decided that I'll do a proper frontend instead using Vue.js. Why Vue.js? Well, it's pretty simple and that's all I need.
As of now, the UI is just going to be a dynamically generated form that then sends a request to the counselor and gets a
ranking of all strands.

I've used Angular in the past with TypeScript but, for this project it feels a little overkill. I'm actually surprised 
with all the things you could do with JavaScript now. And so for my #100DaysOfCode, I'm adjusting my goal a bit from 
just learning Kotlin to creating web applications using Kotlin and JavaScript.
Talky-G
=======

This version of Coron only contains Talky-G and some
other algorithms. It's not the full version.

Input formats
-------------

There are some examples in the `src/main/resources/sample/` folder.
We support three formats: 1) `.basenum`, 2) `.bool`, and 3) `.rcf`.

The format RCF has the advantage that you can assign names
to the attributes.

How to compile
--------------

Install Apache Maven, then execute the command

    $ mvn package

It will create a `target/` folder where you will find
the executable JAR file(s).

Usage
-----

Here are some examples about how to use the software.
It runs in the command-line.

    $ java -jar target/talky-g-1.0-jar-with-dependencies.jar src/main/resources/sample/in.basenum 2 -alg:talkyg2

Parameters:
* input dataset
* `min_supp` value (can be absolute or relative, e.g. `20%` is also accepted)

Options:
* `-names` (used for RCF files to show the attribute names)
* `-alg:ALG` (use the specified algorithm)

Available algorithms in this version:
* `talkyg` (Talky-G, basic version)
* `talkyg2` (Talky-G, improved version)
* `dtalkyg` (Talky-G with diffsets)
* `eclat` (Eclat, basic version)
* `declat` (Eclat with diffsets)
* `talky` (Talky)

Further examples
----------------

    $ java -jar target/talky-g-1.0-jar-with-dependencies.jar src/main/resources/sample/in.bool 2 -alg:talkyg2

    $ java -jar target/talky-g-1.0-jar-with-dependencies.jar src/main/resources/sample/in.rcf 2 -names -alg:talkyg2

Notice the `-names` option when working with RCF datasets.
If you add `-names`, attribute names are shown instead of
attribute numbers.

Bibliography
------------

If you use this software in your research, we ask you to
put a reference on this paper of ours:

    Szathmary, L., Valtchev, P., Napoli, A., Godin, R.: Efficient Vertical Mining of
    Frequent Closures and Generators. In: Proc. of the 8th Intl. Symposium on Intelligent
    Data Analysis (IDA ’09). Volume 5772 of LNCS., Lyon, France, Springer (2009) 393--404

BibTeX entry:

    @inproceedings{szathmary09b,
        Author = {Szathmary, L. and Valtchev, P. and Napoli, A. and Godin, R.},
        Title = {{Efficient Vertical Mining of Frequent Closures and Generators}},
        Booktitle = {Proc. of the 8th Intl. Symposium on Intelligent Data Analysis (IDA '09)},
        Series = {LNCS},
        Volume = {5772},
        Pages = {393--404},
        Address = {Lyon, France},
        Publisher = {Springer},
        Year = 2009
    }

This paper is available online [here](http://www.loria.fr/~szathmar/pmwiki/uploads/Acad/szathmary09c.pdf).


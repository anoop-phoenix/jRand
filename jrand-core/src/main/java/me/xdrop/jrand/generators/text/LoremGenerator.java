package me.xdrop.jrand.generators.text;

import me.xdrop.jrand.CharUtils;
import me.xdrop.jrand.Generator;
import me.xdrop.jrand.annotation.Facade;
import me.xdrop.jrand.data.AssetLoader;
import me.xdrop.jrand.generators.basics.NaturalGenerator;
import me.xdrop.jrand.model.RangeOption;
import me.xdrop.jrand.utils.Choose;

import java.util.List;

@Facade(accessor = "lorem")
public class LoremGenerator extends Generator<String> {
    private NaturalGenerator nat;

    private String introText = "Lorem ipsum dolor sit amet, consectetur adipiscing elit";

    private List<String> loremWords;
    private RangeOption noParagraphs;
    private RangeOption noSentences;
    private RangeOption noWords;
    private boolean capitalize;
    private boolean intro;
    private boolean single;
    private boolean commas;

    public LoremGenerator() {
        this.loremWords = AssetLoader.loadList("lorem.txt");
        this.noParagraphs = RangeOption.from(1);
        this.noSentences = RangeOption.from(5,8);
        this.noWords = RangeOption.from(5,10);
        this.commas = true;
        this.nat = new NaturalGenerator();
    }

    /**
     * Set the number of paragraphs in the lorem
     *
     * @param noParagraphs The number of paragraphs
     * @return The same generator
     */
    public LoremGenerator paragraphs(int noParagraphs) {
        this.noParagraphs = RangeOption.from(noParagraphs);
        return this;
    }

    /**
     * Set the number of paragraphs in the lorem
     *
     * @param min Minimum number of paragraphs (Inclusive)
     * @param max Maximum number of paragraphs (Inclusive)
     * @return The same generator
     */
    public LoremGenerator paragraphs(int min, int max) {
        this.noParagraphs = RangeOption.from(min, max);
        return this;
    }

    /**
     * Set the number of sentencea in the lorem
     *
     * @param noSentences The number of sentences
     * @return The same generator
     */
    public LoremGenerator sentences(int noSentences) {
        this.noSentences = RangeOption.from(noSentences);
        return this;
    }

    /**
     * Set the number of sentences in a paragraph
     *
     * @param min Minimum number of sentences (Inclusive)
     * @param max Maximum number of sentences (Inclusive)
     * @return The same generator
     */
    public LoremGenerator sentences(int min, int max) {
        this.noSentences = RangeOption.from(min, max);
        return this;
    }

    /**
     * Set the number of words in a sentence
     *
     * @param noWords The number of words
     * @return The same generator
     */
    public LoremGenerator words(int noWords) {
        this.noWords = RangeOption.from(noWords);
        return this;
    }

    /**
     * Set the number of words in a sentence
     *
     * @param min Minimum number of words (Inclusive)
     * @param max Maximum number of words (Inclusive)
     * @return The same generator
     */
    public LoremGenerator words(int min, int max) {
        this.noWords = RangeOption.from(min, max);
        return this;
    }

    /**
     * Capitalize the first word
     *
     * @param enabled True for enabled,
     *                False otherwise
     * @return The same generator
     */
    public LoremGenerator capitalize(boolean enabled) {
        this.capitalize = enabled;
        return this;
    }

    /**
     * Capitalize the first word
     *
     * @return The same generator
     */
    public LoremGenerator capitalize() {
        return capitalize(true);
    }

    /**
     * Start with 'Lorem ipsum dolor sit amet...'
     *
     * @param enabled True for enabled,
     *                False otherwise
     * @return The same generator
     */
    public LoremGenerator intro(boolean enabled) {
        this.intro = enabled;
        return this;
    }

    /**
     * Start with 'Lorem ipsum dolor sit amet...'
     *
     * @return The same generator
     */
    public LoremGenerator intro() {
        return intro(true);
    }

    /**
     * Return a single word
     *
     * @param enabled True for enabled,
     *                False otherwise
     * @return The same generator
     */
    public LoremGenerator single(boolean enabled) {
        this.single = enabled;
        return this;
    }

    /**
     * Return a single word
     *
     * @return The same generator
     */
    public LoremGenerator single() {
        return single(true);
    }

    /**
     * Enable/Disable adding commas
     * @param enabled True for enabled,
     *                False for disabled
     * @return The same generator
     */
    public LoremGenerator commas(boolean enabled) {
        this.commas = enabled;
        return this;
    }

    private String word() {
        return Choose.one(loremWords);
    }

    private String sentence(boolean intro) {
        int number = nat.range(noWords).gen();
        StringBuilder sentence = new StringBuilder(64);
        String previousWord = "";

        if (intro) {
            String[] wordsOfIntro = introText.split(" ");
            int n = wordsOfIntro.length;
            int used = 0;
            while (number != 0 && number >= used && used <= n) {
                if (sentence.length() > 0) sentence.append(" ");
                sentence.append(wordsOfIntro[used]);
                previousWord = wordsOfIntro[used];
                number--;
                used++;
            }
        }

        while (number > 0) {
            if (commas && sentence.length() > 0 && nat.range(0,5).gen() == 5) {
                sentence.append(",");
            }
            if (sentence.length() > 0) sentence.append(" ");
            String nextWord;
            // Avoid duplicate words
            while ((nextWord = Choose.one(loremWords)).equals(previousWord)){ }
            sentence.append(nextWord);
            previousWord = nextWord;
            number--;
        }

        sentence.append(". ");

        return CharUtils.capitalize(sentence.toString());
    }

    private String paragraph(boolean intro) {
        int number = nat.range(noSentences).gen();
        StringBuilder paragraph = new StringBuilder(512);

        if (intro) {
            paragraph.append(sentence(true));
            number--;
        }

        while (number > 0) {
            paragraph.append(sentence(false));
            number--;
        }

        paragraph.append("\n\n");

        return paragraph.toString();
    }

    @Override
    public String gen() {
        StringBuilder loremIpsum = new StringBuilder(512);

        if (single) {
            return capitalize ? CharUtils.capitalize(word()) : word();
        }

        int number = nat.range(noParagraphs).gen();

        if (intro) {
            loremIpsum.append(paragraph(true));
            number--;
        }

        while (number > 0) {
            loremIpsum.append(paragraph(false));
            number--;
        }

        return loremIpsum.toString();

    }
}

import org.example.model.Person;
import org.example.service.CedacriPersonValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

public class TestPerson {

    @ParameterizedTest
    @ValueSource(strings = {"John", "Alex Pereira", "NameSurname", "Name MiddleName Surname"})
    public void givenValidName_whenIsValid_returnTrue(String name) {
        Person person = new Person(name, "+37312345678");
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"John123", "Alex %%%", "123ASd"})
    public void givenInvalidName_whenIsValid_returnFalse(String name) {
        Person person = new Person(name, "+37312345678");
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @ValueSource(strings = {" ", "\t", "\n", "\u000B", "\f", "\r"})
    public void givenNullEmptyAndBlankName_whenIsValid_returnFalse(String name) {
        Person person = new Person(name, "+37312345678");
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    public void givenNullEmptyPhone_whenIsValid_returnFalse(String phone) {
        Person person = new Person("John", phone);
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"+15555555555", "not a phone number", "+40123456789", "  "})
    public void givenNonItalianOrNonMoldavianPrefixPhoneString_whenIsValid_returnFalse(String phone) {
        Person person = new Person("John Doe", phone);
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @Test
    public void givenMoldavianPrefixPhone_whenIsValid_returnTrue() {
        Person person = new Person("John Doe", "+37312345678");
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertTrue(result);
    }

    @Test
    public void givenMoldavianPrefixWithIrregularLengthOfDigitsPhone_whenIsValid_returnFalse() {
        Person person = new Person("John Doe", "+373123456781");
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"+3912345", "+391234567891234"})
    public void givenItalianPrefixWithIrregularLengthOfDigitsPhone_whenIsValid_returnFalse(String phone) {
        Person person = new Person("John Doe", phone);
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"+39123456", "+39123456789123"})
    public void givenItalianPrefixPhoneWithRegularLengthOfDigits_whenIsValid_returnTrue(String phone) {
        Person person = new Person("John Doe", phone);
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertTrue(result);
    }

    @ParameterizedTest
    @ValueSource(strings = {"+39 123456", "+39 123456789123", "+373 12345678"})
    public void givenItalianPrefixOrMoldavianPrefixWithSpacesBetweenDigitsPhone_whenIsValid_returnFalse(String phone) {
        Person person = new Person("John Doe", phone);
        CedacriPersonValidator validator =  new CedacriPersonValidator();
        boolean result = validator.isValid(person);
        assertFalse(result);
    }

    @ParameterizedTest
    @CsvSource({
            "+39123456,39123456000000",
            "+39123456789123,39123456789123",
            "+37312345678,37312345678000"
    })
    public void givenPhone_whenGetRating_returnRating(String phone, long expectedRating) {
        Person person = new Person("John", phone);
        long result = person.getRating();
        assertEquals(expectedRating, result);
    }

    @ParameterizedTest
    @NullSource
    public void givenNullPhone_whenGetRating_throwsInvalidArgumentException(String phone) {
        Person person = new Person("John", phone);
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, person::getRating);
        assertEquals("Phone must not bet null", exception.getMessage());
    }

    @Test
    public void givenMoldavianPrefixPhone_whenIsGood_returnTrue() {
        Person person = new Person("John Doe", "+37312345678");
        boolean result = person.isGood();
        assertTrue(result);
    }

    @Test
    public void givenItalianPrefixPhone_whenIsGood_returnFalse() {
        Person person = new Person("John Doe", "+39123456");
        boolean result = person.isGood();
        assertFalse(result);
    }

    @Test
    public void givenNullPhone_whenIsGood_returnFalse() {
        Person person = new Person("John Doe", null);
        boolean result = person.isGood();
        assertFalse(result);
    }
}

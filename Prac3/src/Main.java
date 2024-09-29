import java.util.List;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

// Клас Company з полями parent і employeeCount
class Company {
    private Company parent;
    private long employeeCount;

    // Getters and Setters
    public Company getParent() {
        return parent;
    }

    public void setParent(Company parent) {
        this.parent = parent;
    }

    public long getEmployeeCount() {
        return employeeCount;
    }

    public void setEmployeeCount(long employeeCount) {
        this.employeeCount = employeeCount;
    }
}

// Інтерфейс ICompanyService
interface ICompanyService {

    /**
     * @param child – компанія, для якої шукаємо верхній рівень батька
     * @return верхній рівень батька (top-level parent)
     */
    Company getTopLevelParent(Company child);

    /**
     * @param company – компанія, для якої шукаємо кількість працівників
     * @param companies – всі доступні компанії
     * @return кількість працівників
     */
    long getEmployeeCountForCompanyAndChildren(Company company, List<Company> companies);
}

// Реалізація ICompanyService
class CompanyServiceImpl implements ICompanyService {

    @Override
    public Company getTopLevelParent(Company child) {
        if (child == null || child.getParent() == null) {
            return null;
        }
        Company topLevelParent = child;
        while (topLevelParent.getParent() != null) {
            topLevelParent = topLevelParent.getParent();
        }
        return topLevelParent;
    }

    @Override
    public long getEmployeeCountForCompanyAndChildren(Company company, List<Company> companies) {
        if (company == null) {
            throw new NullPointerException("Company cannot be null");
        }

        long totalEmployeeCount = company.getEmployeeCount();
        for (Company c : companies) {
            if (c.getParent() == company) {
                totalEmployeeCount += getEmployeeCountForCompanyAndChildren(c, companies);
            }
        }
        return totalEmployeeCount;
    }
}
class CompanyServiceTest {

    private ICompanyService companyService;
    private Company parent;
    private Company child1;
    private Company child2;
    private Company grandchild1;
    private List<Company> companies;

    @BeforeEach
    void setUp() {
        companyService = new CompanyServiceImpl();
        parent = new Company();
        child1 = new Company();
        child2 = new Company();
        grandchild1 = new Company();

        // Налаштування ієрархії
        child1.setParent(parent);
        child2.setParent(parent);
        grandchild1.setParent(child1);

        // Налаштування кількості працівників
        parent.setEmployeeCount(100);
        child1.setEmployeeCount(50);
        child2.setEmployeeCount(75);
        grandchild1.setEmployeeCount(30);

        companies = new ArrayList<>();
        companies.add(parent);
        companies.add(child1);
        companies.add(child2);
        companies.add(grandchild1);
    }

    @Test
    void testGetTopLevelParentForGrandchild() {
        // 1. Перевіряє, що для "grandchild1" верхній рівень батька (top-level parent) – це компанія "parent".
        Company topLevelParent = companyService.getTopLevelParent(grandchild1);
        assertEquals(parent, topLevelParent, "Top-level parent of grandchild should be the parent company.");
    }

    @Test
    void testGetTopLevelParentForChild() {
        // 2. Перевіряє, що для "child1" верхній рівень батька – це компанія "parent".
        Company topLevelParent = companyService.getTopLevelParent(child1);
        assertEquals(parent, topLevelParent, "Top-level parent of child1 should be the parent company.");
    }

    @Test
    void testGetTopLevelParentForParent() {
        // 3. Перевіряє, що компанія "parent" не має батька, тобто верхній рівень батька має бути null.
        Company topLevelParent = companyService.getTopLevelParent(parent);
        assertNull(topLevelParent, "Top-level parent of the parent should be null.");
    }

    @Test
    void testGetTopLevelParentForCompanyWithNoParent() {
        // 4. Тестує компанію без батьків, що повертає null, оскільки вона не має батька.
        Company independent = new Company();
        Company topLevelParent = companyService.getTopLevelParent(independent);
        assertNull(topLevelParent, "Independent company with no parent should return null as top-level parent.");
    }

    @Test
    void testGetEmployeeCountForParentAndChildren() {
        // 5. Перевіряє, що загальна кількість працівників включає "parent" та всі дочірні компанії.
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(parent, companies);
        assertEquals(255, totalEmployeeCount, "Total employee count should be sum of parent and all children.");
    }

    @Test
    void testGetEmployeeCountForChildWithChildren() {
        // 6. Перевіряє загальну кількість працівників для "child1", яка включає її працівників і працівників її дочірніх компаній.
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(child1, companies);
        assertEquals(80, totalEmployeeCount, "Total employee count should include child1 and its children.");
    }

    @Test
    void testGetEmployeeCountForChildWithNoChildren() {
        // 7. Перевіряє кількість працівників для "child2", яка не має дочірніх компаній.
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(child2, companies);
        assertEquals(75, totalEmployeeCount, "Total employee count for child2 with no children should be its own count.");
    }

    @Test
    void testGetEmployeeCountForCompanyWithNoChildren() {
        // 8. Перевіряє кількість працівників для незалежної компанії без дочірніх компаній.
        Company independent = new Company();
        independent.setEmployeeCount(25);
        companies.add(independent);
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(independent, companies);
        assertEquals(25, totalEmployeeCount, "Total employee count for an independent company should be its own count.");
    }

    @Test
    void testGetEmployeeCountWithEmptyCompanyList() {
        // 9. Перевіряє кількість працівників для "parent", коли список компаній порожній.
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(parent, new ArrayList<>());
        assertEquals(100, totalEmployeeCount, "Total employee count should return just the parent count if company list is empty.");
    }

    @Test
    void testGetEmployeeCountForCompanyNotInList() {
        // 10. Перевіряє, що кількість працівників компанії, яка не входить до списку, повертається правильно.
        Company otherCompany = new Company();
        otherCompany.setEmployeeCount(60);
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(otherCompany, companies);
        assertEquals(60, totalEmployeeCount, "Total employee count should return just the company count if it's not in the list.");
    }

    @Test
    void testSetAndGetEmployeeCount() {
        // 11. Перевіряє, що кількість працівників для "child1" оновлюється та відображається коректно.
        child1.setEmployeeCount(60);
        assertEquals(60, child1.getEmployeeCount(), "Employee count should reflect the updated value.");
    }

    @Test
    void testSetAndGetParent() {
        // 12. Перевіряє, що батько "child1" успішно змінюється на "child2".
        child1.setParent(child2);
        assertEquals(child2, child1.getParent(), "Parent of child1 should be updated to child2.");
    }

    @Test
    void testNoParentForCompanyInitially() {
        // 13. Перевіряє, що нова компанія спочатку не має батька.
        Company newCompany = new Company();
        assertNull(newCompany.getParent(), "New company should have no parent initially.");
    }

    @Test
    void testParentAfterSettingNull() {
        // 14. Перевіряє, що після того, як встановити батька в null, у "child1" батько буде null.
        child1.setParent(null);
        assertNull(child1.getParent(), "Parent should be null after setting it to null.");
    }

    @Test
    void testEmployeeCountForCompanyWithMultipleLevelsOfChildren() {
        // 15. Перевіряє кількість працівників для компанії з декількома рівнями дочірніх компаній.
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(child1, companies);
        assertEquals(80, totalEmployeeCount, "Total employee count should include employees of all levels of children.");
    }

    @Test
    void testEmployeeCountForCompanyWithoutParentInList() {
        // 16. Перевіряє кількість працівників, якщо батько не входить до списку компаній.
        companies.remove(parent);
        long totalEmployeeCount = companyService.getEmployeeCountForCompanyAndChildren(child1, companies);
        assertEquals(80, totalEmployeeCount, "Total employee count should include children even if parent is removed from the list.");
    }

    @Test
    void testGetTopLevelParentWhenParentIsNull() {
        // 17. Перевіряє, що коли батько у компанії null, метод повертає null.
        child1.setParent(null);
        assertNull(companyService.getTopLevelParent(child1), "If a company's parent is null, the top-level parent should be null.");
    }
    
    @Test
    void testGetTopLevelParentWhenCompanyHasOneParent() {
        // 18. Перевіряє, що метод повертає "топ-рівневого" батька, якщо у компанії є батько.
        Company parent = new Company();
        child1.setParent(parent); // Установлюємо батька для child1

        // Очікуємо, що метод поверне батька
        assertEquals(parent, companyService.getTopLevelParent(child1),
                "The top-level parent should be the parent company when there is only one parent.");
    }

    @Test
    void testEmployeeCountForCircularParentReferences() {
        // 19. Перевіряє, що кругові посилання батько-дитина викликають StackOverflowError під час підрахунку працівників.
        child1.setParent(grandchild1); // Creating a circular reference
        assertThrows(StackOverflowError.class, () -> companyService.getEmployeeCountForCompanyAndChildren(grandchild1, companies),
                "A circular parent reference should cause a StackOverflowError.");
    }

    @Test
    void testGetEmployeeCountForNullCompany() {
        // 20. Перевіряє, що передача null як компанії викликає NullPointerException.
        assertThrows(NullPointerException.class, () -> companyService.getEmployeeCountForCompanyAndChildren(null, companies),
                "Passing null as a company should throw a NullPointerException.");
    }
}

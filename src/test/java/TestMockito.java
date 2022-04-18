import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import ru.netology.entity.Country;
import ru.netology.entity.Location;
import ru.netology.geo.GeoService;
import ru.netology.geo.GeoServiceImpl;
import ru.netology.i18n.LocalizationService;
import ru.netology.i18n.LocalizationServiceImpl;
import ru.netology.sender.MessageSender;
import ru.netology.sender.MessageSenderImpl;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static ru.netology.entity.Country.*;

public class TestMockito {

    static GeoService geo;
    static LocalizationServiceImpl loc;
    static MessageSender mess;
    Map<String, String> headers = new HashMap<String, String>();

    Location russia = new Location("Moscow", Country.RUSSIA, "Lenina", 15);
    Location usa = new Location("New York", Country.USA, " 10th Avenue", 32);
    String welcomeRussian = "Добро пожаловать";
    String welcomeEnglish = "Welcome";

    // for Mockito
    static GeoService geoService;
    static LocalizationService localizationService;

    @BeforeAll
    public static void init() {
        loc = new LocalizationServiceImpl();
        geo = new GeoServiceImpl();
        // Mock for GeoServiceImpl
        geoService = Mockito.mock(GeoServiceImpl.class);
        // Mock for LocalizationServiceImpl
        localizationService = Mockito.mock(LocalizationServiceImpl.class);
        // MessageSenderImpl
        mess = new MessageSenderImpl(geo, loc);
    }

    // Russian message if Russia
    @Test
    void test_if_location_ru() {
        String result = loc.locale(RUSSIA);
        assertEquals(welcomeRussian, result);
    }

    // English message if USA
    @Test
    void test_if_location_us() {
        String result = loc.locale(USA);
        assertEquals(welcomeEnglish, result);
    }

    // test for: Location byIp method, locale method
    @Test
    void test_for_Location_byIp_ru() {
        Mockito.when(geoService.byIp(Mockito.startsWith("172.")))
                .thenReturn(russia);
    }

    @Test
    void test_for_Location_byIp_en() {
        Mockito.when(geoService.byIp("96.44.183.149"))
                .thenReturn(usa);
    }

    @Test
    void test_for_String_locale() {
        Mockito.when(localizationService.locale(RUSSIA))
                .thenReturn(welcomeRussian);
        Mockito.when(localizationService.locale(USA))
                .thenReturn(welcomeEnglish);
    }

    // if MessageSenderImpl sends Russian text
    @Test
    void test_if_MessageSender_sends_ru() {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "172.0.32.11");
        assertEquals(welcomeRussian, mess.send(headers));
    }
    // if MessageSenderImpl sends English text
    @Test
    void text_if_MessageSender_sends_en() {
        headers.put(MessageSenderImpl.IP_ADDRESS_HEADER, "96.44.183.149");
        assertEquals(welcomeEnglish, mess.send(headers));
    }
}

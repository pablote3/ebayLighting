package com.rossotti.ebay.client.account;

import com.rossotti.ebay.config.AppConfig;
import com.rossotti.ebay.config.ServerConfig;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicies;
import com.rossotti.ebay.model.account.paymentPolicy.PaymentPolicy;
import com.rossotti.ebay.util.TestUtil;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import okhttp3.mockwebserver.RecordedRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.util.Optional;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import static com.rossotti.ebay.model.common.CategoryTypeEnum.ALL_EXCLUDING_MOTORS_VEHICLES;
import static com.rossotti.ebay.model.common.MarketplaceIdEnum.EBAY_US;
import static com.rossotti.ebay.model.account.paymentPolicy.PaymentInstrumentBrandEnum.AMERICAN_EXPRESS;
import static com.rossotti.ebay.model.account.paymentPolicy.PaymentInstrumentBrandEnum.MASTERCARD;
import static com.rossotti.ebay.model.account.paymentPolicy.PaymentInstrumentBrandEnum.VISA;
import static com.rossotti.ebay.model.account.paymentPolicy.PaymentMethodTypeEnum.PAYPAL;
import static com.rossotti.ebay.model.account.paymentPolicy.PaymentMethodTypeEnum.CREDIT_CARD;
import static com.rossotti.ebay.model.account.paymentPolicy.RecipientAccountReferenceTypeEnum.PAYPAL_EMAIL;
import static com.rossotti.ebay.model.common.TimeDurationUnitEnum.DAY;

@SpringBootTest
public class PaymentPolicyClientTests {
    private static final String PAYMENT_POLICY_JSON = "data/account/paymentPolicy.json";
    private static final String PAYMENT_POLICIES_JSON = "data/account/paymentPolicies.json";
    private static final String GET = "GET";
    private static MockWebServer mockWebServer;
    @Autowired
    private AppConfig appConfig;
    @Autowired
    private PaymentPolicyClient paymentPolicyClient;

    @BeforeEach
    public void setup() {
        mockWebServer = new MockWebServer();
        ServerConfig serverConfig = TestUtil.createServerConfig(mockWebServer.url("/"));
        paymentPolicyClient = new PaymentPolicyClient(WebClient.create(), appConfig, serverConfig);
    }

    @AfterAll
    public static void tearDown() throws IOException {
        mockWebServer.shutdown();
    }

    @Test
    void paymentPolicy_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(PAYMENT_POLICY_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        paymentPolicyClient.getByPaymentPolicyId("6196932000");
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/payment_policy/6196932000?marketplace_id=EBAY_US"));
   }

    @Test
    void paymentPolicy_responseDeserialization() {
        String json = TestUtil.readStringFromFile(PAYMENT_POLICY_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
            new MockResponse()
                .setResponseCode(200)
                .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .setBody(json)
        );
        Optional<PaymentPolicy> response = paymentPolicyClient.getByPaymentPolicyId("6196932000");

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getName(), is("CreditCard"));
        assertThat(response.get().getDescription(), is("CreditCard"));
        assertThat(response.get().getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getCategoryTypes().get(0).getDefaultValue(), is(false));
        assertThat(response.get().getPaymentMethods().get(0).getPaymentMethodType(), is(CREDIT_CARD));
        assertThat(response.get().getPaymentMethods().get(0).getBrands(), hasSize(3));
        assertThat(response.get().getPaymentMethods().get(0).getBrands().get(0), is(AMERICAN_EXPRESS));
        assertThat(response.get().getPaymentMethods().get(0).getBrands().get(1), is(VISA));
        assertThat(response.get().getPaymentMethods().get(0).getBrands().get(2), is(MASTERCARD));
        assertThat(response.get().getFullPaymentDueIn().getValue(), is(7));
        assertThat(response.get().getFullPaymentDueIn().getUnit(), is(DAY));
        assertThat(response.get().getImmediatePay(), is(false));
    }

    @Test
    void paymentPolicies_requestSerialization() throws InterruptedException {
        String str = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
        assertThat(str, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(str)
        );
        paymentPolicyClient.getPaymentPolicies();
        RecordedRequest request = mockWebServer.takeRequest();

        assertThat(request.getMethod(), is(GET));
        assertThat(request.getPath(), is("/sell/account/v1/payment_policy?marketplace_id=EBAY_US"));
    }

    @Test
    void paymentPolicies_responseDeserialization() {
        String json = TestUtil.readStringFromFile(PAYMENT_POLICIES_JSON).orElse(null);
        assertThat(json, is(notNullValue()));
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody(json)
        );
        Optional<PaymentPolicies> response = paymentPolicyClient.getPaymentPolicies();

        assertThat(response.isPresent(), is(true));
        assertThat(response.get().getPaymentPolicies(), hasSize(2));
        assertThat(response.get().getTotal(), is(2));
        assertThat(response.get().getPaymentPolicies().get(0).getName(), is("CreditCard"));
        assertThat(response.get().getPaymentPolicies().get(0).getDescription(), is("CreditCard"));
        assertThat(response.get().getPaymentPolicies().get(0).getMarketplaceId(), is(EBAY_US));
        assertThat(response.get().getPaymentPolicies().get(0).getCategoryTypes().get(0).getName(), is(ALL_EXCLUDING_MOTORS_VEHICLES));
        assertThat(response.get().getPaymentPolicies().get(0).getCategoryTypes().get(0).getDefaultValue(), is(false));
        assertThat(response.get().getPaymentPolicies().get(0).getPaymentMethods().get(0).getPaymentMethodType(), is(CREDIT_CARD));
        assertThat(response.get().getPaymentPolicies().get(0).getPaymentMethods().get(0).getBrands(), hasSize(3));
        assertThat(response.get().getPaymentPolicies().get(0).getPaymentMethods().get(0).getBrands().get(0), is(AMERICAN_EXPRESS));
        assertThat(response.get().getPaymentPolicies().get(0).getPaymentMethods().get(0).getBrands().get(1), is(VISA));
        assertThat(response.get().getPaymentPolicies().get(0).getPaymentMethods().get(0).getBrands().get(2), is(MASTERCARD));
        assertThat(response.get().getPaymentPolicies().get(0).getFullPaymentDueIn().getValue(), is(7));
        assertThat(response.get().getPaymentPolicies().get(0).getFullPaymentDueIn().getUnit(), is(DAY));
        assertThat(response.get().getPaymentPolicies().get(0).getImmediatePay(), is(false));
        assertThat(response.get().getPaymentPolicies().get(1).getName(), is("Paypal"));
        assertThat(response.get().getPaymentPolicies().get(1).getDescription(), is("Paypal"));
        assertThat(response.get().getPaymentPolicies().get(1).getPaymentMethods().get(0).getPaymentMethodType(), is(PAYPAL));
        assertThat(response.get().getPaymentPolicies().get(1).getPaymentMethods().get(0).getRecipientAccountReference().getReferenceType(), is(PAYPAL_EMAIL));
        assertThat(response.get().getPaymentPolicies().get(1).getFullPaymentDueIn().getValue(), is(7));
        assertThat(response.get().getPaymentPolicies().get(1).getFullPaymentDueIn().getUnit(), is(DAY));
        assertThat(response.get().getPaymentPolicies().get(1).getImmediatePay(), is(false));
    }
}

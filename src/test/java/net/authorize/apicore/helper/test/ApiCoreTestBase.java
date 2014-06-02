package net.authorize.apicore.helper.test;

import java.lang.reflect.Constructor;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;

import javax.xml.datatype.DatatypeFactory;

import junit.framework.Assert;
import net.authorize.Environment;
import net.authorize.Merchant;
import net.authorize.UnitTestData;
import net.authorize.apicore.contract.v1.ANetApiRequest;
import net.authorize.apicore.contract.v1.ANetApiResponse;
import net.authorize.apicore.contract.v1.ARBCreateSubscriptionResponse;
import net.authorize.apicore.contract.v1.ARBSubscriptionType;
import net.authorize.apicore.contract.v1.ARBSubscriptionUnitEnum;
import net.authorize.apicore.contract.v1.BankAccountType;
import net.authorize.apicore.contract.v1.BankAccountTypeEnum;
import net.authorize.apicore.contract.v1.CreditCardTrackType;
import net.authorize.apicore.contract.v1.CreditCardType;
import net.authorize.apicore.contract.v1.CustomerAddressType;
import net.authorize.apicore.contract.v1.CustomerPaymentProfileType;
import net.authorize.apicore.contract.v1.CustomerProfileType;
import net.authorize.apicore.contract.v1.CustomerType;
import net.authorize.apicore.contract.v1.CustomerTypeEnum;
import net.authorize.apicore.contract.v1.DriversLicenseType;
import net.authorize.apicore.contract.v1.EcheckTypeEnum;
import net.authorize.apicore.contract.v1.EncryptedTrackDataType;
import net.authorize.apicore.contract.v1.KeyBlock;
import net.authorize.apicore.contract.v1.MerchantAuthenticationType;
import net.authorize.apicore.contract.v1.MessageTypeEnum;
import net.authorize.apicore.contract.v1.NameAndAddressType;
import net.authorize.apicore.contract.v1.OrderType;
import net.authorize.apicore.contract.v1.PayPalType;
import net.authorize.apicore.contract.v1.PaymentScheduleType;
import net.authorize.apicore.contract.v1.PaymentType;
import net.authorize.apicore.controller.ARBCreateSubscriptionController;
import net.authorize.apicore.controller.base.ApiOperationBase;
import net.authorize.data.xml.reporting.ReportingDetails;
import net.authorize.util.Constants;
import net.authorize.util.DateUtil;
import net.authorize.util.LogHelper;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

public abstract class ApiCoreTestBase {

	protected static Log logger = LogFactory.getLog(ApiCoreTestBase.class);
	
	static Environment environment = Environment.HOSTED_VM;
	static Merchant cnpMerchant = null;
	static Merchant cpMerchant = null;
	static String CnpApiLoginIdKey = null;
	static String CnpTransactionKey = null;
	static String CnpMd5HashKey = null;
	static String CpApiLoginIdKey = null;
	static String CpTransactionKey = null;
	static String CpMd5HashKey = null;
	
	DatatypeFactory datatypeFactory = null;
	GregorianCalendar pastDate = null;
	GregorianCalendar nowDate = null;
	GregorianCalendar futureDate = null;
	String nowString = null;
	Date now = null;

	String refId = null;
	int counter = 0;
	String counterStr = null;

	MerchantAuthenticationType cnpMerchantAuthenticationType = null;
	MerchantAuthenticationType cpMerchantAuthenticationType = null;

	ARBSubscriptionType arbSubscriptionOne = null;
	ARBSubscriptionType arbSubscriptionTwo = null;
	BankAccountType bankAccountOne = null;
	CreditCardTrackType trackDataOne = null;
	CreditCardType creditCardOne = null;
	CustomerAddressType customerAddressOne = null;
	CustomerPaymentProfileType customerPaymentProfileOne = null;
	CustomerProfileType customerProfileType = null;
	CustomerType customerOne = null;
	CustomerType customerTwo = null;
	DriversLicenseType driversLicenseOne = null; 
	EncryptedTrackDataType encryptedTrackDataOne = null;
	NameAndAddressType nameAndAddressTypeOne = null;
	NameAndAddressType nameAndAddressTypeTwo = null;
	PaymentScheduleType paymentScheduleTypeOne = null;
	PaymentType paymentOne = null;
	PayPalType payPalOne = null;
	
	private Random random = new Random();
	static {
		//getPropertyFromNames get the value from properties file or environment
		CnpApiLoginIdKey = UnitTestData.getPropertyFromNames(Constants.ENV_API_LOGINID, Constants.PROP_API_LOGINID);
		CnpTransactionKey = UnitTestData.getPropertyFromNames(Constants.ENV_TRANSACTION_KEY, Constants.PROP_TRANSACTION_KEY);
		CnpMd5HashKey = null;
		CpApiLoginIdKey = UnitTestData.getPropertyFromNames(Constants.ENV_CP_API_LOGINID, Constants.PROP_CP_API_LOGINID);
		CpTransactionKey = UnitTestData.getPropertyFromNames(Constants.ENV_CP_TRANSACTION_KEY, Constants.PROP_CP_TRANSACTION_KEY);
		CpMd5HashKey = UnitTestData.getPropertyFromNames(Constants.ENV_MD5_HASHKEY, Constants.PROP_MD5_HASHKEY);

		if ((null == CnpApiLoginIdKey) ||
			(null == CnpTransactionKey) ||
			(null == CpApiLoginIdKey) ||
			(null == CpTransactionKey))
		{
			throw new IllegalArgumentException("LoginId and/or TransactionKey have not been set.");
		}
		
		//hosted vm
		CnpApiLoginIdKey = "7zc5c7YBTE";
		CnpTransactionKey = "5kPE8v6wdL6Dj56V";
		
		cnpMerchant = Merchant.createMerchant( environment, CnpApiLoginIdKey, CnpTransactionKey);
		cpMerchant = Merchant.createMerchant( environment, CpApiLoginIdKey, CpTransactionKey);
		
	}
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		//initialize counter
		counter = random.nextInt((int) Math.pow(2, 24));
		counterStr = getRandomString("");

		now = Calendar.getInstance().getTime();
		nowString = DateUtil.getFormattedDate(now, ReportingDetails.DATE_FORMAT);

		datatypeFactory = DatatypeFactory.newInstance();
		//TODO add / substract relative
		pastDate = new GregorianCalendar(2010, 01, 01);
		nowDate = new GregorianCalendar();
		futureDate = new GregorianCalendar(2020, 12, 31);
		
		cnpMerchantAuthenticationType = new MerchantAuthenticationType() ;
		cnpMerchantAuthenticationType.setName(CnpApiLoginIdKey);
		cnpMerchantAuthenticationType.setTransactionKey(CnpTransactionKey);

		cpMerchantAuthenticationType = new MerchantAuthenticationType() ;
		cpMerchantAuthenticationType.setName(CpApiLoginIdKey);
		cpMerchantAuthenticationType.setTransactionKey(CpTransactionKey);
		
//		merchantAuthenticationType.setSessionToken(getRandomString("SessionToken"));
//		merchantAuthenticationType.setPassword(getRandomString("Password"));
//	    merchantAuthenticationType.setMobileDeviceId(getRandomString("MobileDevice"));
	    
//	    ImpersonationAuthenticationType impersonationAuthenticationType = new ImpersonationAuthenticationType();
//	    impersonationAuthenticationType.setPartnerLoginId(CnpApiLoginIdKey);
//	    impersonationAuthenticationType.setPartnerTransactionKey(CnpTransactionKey);
//	    merchantAuthenticationType.setImpersonationAuthentication(impersonationAuthenticationType);

		customerProfileType = new CustomerProfileType() ;
		customerProfileType.setMerchantCustomerId(getRandomString("Customer"));
		customerProfileType.setDescription(getRandomString("CustomerDescription"));
		customerProfileType.setEmail(counterStr+".customerProfileType@test.anet.net");
		
		//make sure these elements are initialized by calling get as it uses lazy initialization
		//List<CustomerPaymentProfileType> paymentProfiles = 
				customerProfileType.getPaymentProfiles();
		//List<CustomerAddressType> addresses = 
				customerProfileType.getShipToList();
		
		CreditCardType creditCardOne = new CreditCardType();
		creditCardOne.setCardNumber("4111111111111111");
		creditCardOne.setExpirationDate("2038-12");
//		creditCardOne.setCardCode("");

		BankAccountType bankAccountOne = new BankAccountType();
		bankAccountOne.setAccountType(BankAccountTypeEnum.SAVINGS);
		bankAccountOne.setRoutingNumber("125000000");
		bankAccountOne.setAccountNumber(getRandomString("A/C#"));
		bankAccountOne.setNameOnAccount((getRandomString("A/CName")));
		bankAccountOne.setEcheckType(EcheckTypeEnum.WEB); 
		bankAccountOne.setBankName(getRandomString("Bank"));
		bankAccountOne.setCheckNumber(counterStr);
	    
		CreditCardTrackType trackDataOne = new CreditCardTrackType();
		trackDataOne.setTrack1(getRandomString("Track1"));
		trackDataOne.setTrack2(getRandomString("Track2"));

		EncryptedTrackDataType encryptedTrackDataOne = new EncryptedTrackDataType();
		KeyBlock keyBlock = new KeyBlock();
		//keyBlock.setValue(value);
		encryptedTrackDataOne.setFormOfPayment(keyBlock);

		payPalOne = new PayPalType();
		payPalOne.setSuccessUrl(getRandomString("http://success.anet.net"));
		payPalOne.setCancelUrl(getRandomString("http://cancel.anet.net"));
		payPalOne.setPaypalLc(getRandomString("Lc"));
		payPalOne.setPaypalHdrImg(getRandomString("Hdr"));
		payPalOne.setPaypalPayflowcolor(getRandomString("flowClr"));
		payPalOne.setPayerID(getRandomString("PayerId"));
		
		paymentOne = new PaymentType();
		paymentOne.setCreditCard(creditCardOne);
		//paymentOne.setBankAccount(bankAccountOne);
		//paymentOne.setTrackData(trackDataOne);
		//paymentOne.setEncryptedTrackData(encryptedTrackDataOne);
		//paymentOne.setPayPal( payPalOne);
		
//		driversLicenseOne = new DriversLicenseType();
//		driversLicenseOne.setNumber(getRandomString("DLNumber"));
//		driversLicenseOne.setState(getRandomString("WA"));
//		driversLicenseOne.setDateOfBirth(nowString);

		customerAddressOne = new CustomerAddressType();
		customerAddressOne.setFirstName(getRandomString("FName"));
		customerAddressOne.setLastName(getRandomString("LName"));
	    customerAddressOne.setCompany(getRandomString("Company"));
	    customerAddressOne.setAddress(getRandomString("StreetAdd"));
	    customerAddressOne.setCity("Bellevue");
	    customerAddressOne.setState("WA");
	    customerAddressOne.setZip("98000");
	    customerAddressOne.setCountry("USA");
	    customerAddressOne.setPhoneNumber(formatToPhone(counter));
	    customerAddressOne.setFaxNumber(formatToPhone(counter+1));
		
		customerPaymentProfileOne = new CustomerPaymentProfileType();
		customerPaymentProfileOne.setCustomerType(CustomerTypeEnum.INDIVIDUAL);
		customerPaymentProfileOne.setPayment(paymentOne);
//	    customerPaymentProfileOne.setBillTo(customerAddressOne);
//	    customerPaymentProfileOne.setDriversLicense(driversLicenseOne);
//	    customerPaymentProfileOne.setTaxId(getRandomString("XX"));


		customerOne = new CustomerType();
		customerOne.setType(CustomerTypeEnum.INDIVIDUAL);
		customerOne.setId(getRandomString("Id"));
		customerOne.setEmail(counterStr+".customerOne@test.anet.net");
		customerOne.setPhoneNumber(formatToPhone(counter));
		customerOne.setFaxNumber(formatToPhone(counter+1));
		customerOne.setDriversLicense(driversLicenseOne);
		customerOne.setTaxId("911011011");//"123-45-6789");//TODO

		customerTwo = new CustomerType();

		PaymentScheduleType.Interval interval = new PaymentScheduleType.Interval();
		interval.setLength( (short)1);
		interval.setUnit(ARBSubscriptionUnitEnum.MONTHS);
		
		OrderType orderType = new OrderType();
		//TODO ADD VALIDATION ON INVOICE LENGTH
		orderType.setInvoiceNumber(getRandomString("Inv:")); 
		orderType.setDescription(getRandomString("Description"));

		nameAndAddressTypeOne = new NameAndAddressType ();
		nameAndAddressTypeOne.setFirstName(getRandomString("FName"));
		nameAndAddressTypeOne.setLastName(getRandomString("LName"));
		nameAndAddressTypeOne.setCompany(getRandomString("Company"));
		nameAndAddressTypeOne.setAddress(getRandomString("Address"));
		nameAndAddressTypeOne.setCity(getRandomString("City"));
		nameAndAddressTypeOne.setState(getRandomString("State"));
		nameAndAddressTypeOne.setZip("98004");
		nameAndAddressTypeOne.setCountry("USA");
		
		nameAndAddressTypeTwo = new NameAndAddressType ();
		nameAndAddressTypeTwo.setFirstName(getRandomString("FName"));
		nameAndAddressTypeTwo.setLastName(getRandomString("LName"));
		nameAndAddressTypeTwo.setCompany(getRandomString("Company"));
		nameAndAddressTypeTwo.setAddress(getRandomString("Address"));
		nameAndAddressTypeTwo.setCity(getRandomString("City"));
		nameAndAddressTypeTwo.setState(getRandomString("State"));
		nameAndAddressTypeTwo.setZip("98004");
		nameAndAddressTypeTwo.setCountry("USA");
		
		paymentScheduleTypeOne = new PaymentScheduleType();
		paymentScheduleTypeOne.setInterval(interval);
		paymentScheduleTypeOne.setStartDate(datatypeFactory.newXMLGregorianCalendar(nowDate));
		paymentScheduleTypeOne.setTotalOccurrences((short)5);
		paymentScheduleTypeOne.setTrialOccurrences((short)0);
		
		arbSubscriptionOne = new ARBSubscriptionType();
		arbSubscriptionOne.setAmount( setValidAmount(counter));
		arbSubscriptionOne.setBillTo(nameAndAddressTypeOne);
		arbSubscriptionOne.setCustomer(customerOne);
		arbSubscriptionOne.setName(getRandomString("Name"));
		arbSubscriptionOne.setOrder(orderType);
		arbSubscriptionOne.setPayment(paymentOne);
		arbSubscriptionOne.setPaymentSchedule(paymentScheduleTypeOne);
		arbSubscriptionOne.setShipTo(nameAndAddressTypeOne);
		arbSubscriptionOne.setTrialAmount(setValidAmount(0));
		
	    refId = counterStr;
	}

	@After
	public void tearDown() throws Exception {
	}

	String getRandomString(String title) {
		return String.format("%s%d", title, counter);

	}
	
	public String formatToPhone(int number) {
		DecimalFormat formatter = new DecimalFormat( "0000000000");
		String formattedNumber = formatter.format(number).toString();
		return 	formattedNumber.substring(0, 3)+"-"+
				formattedNumber.substring(3, 6)+"-"+
				formattedNumber.substring(6, 10);
	}
	
	public BigDecimal setValidAmount(int number) {
		return new BigDecimal( number > MAX_AMOUNT ? (number%MAX_AMOUNT) : number);
	}
	
	private int MAX_AMOUNT = 1000;//214747;

	@SuppressWarnings("unchecked")
	protected static <Q extends ANetApiRequest, S extends ANetApiResponse, T extends ApiOperationBase<Q, S>> S executeTestRequestWithSuccess(Q request, Class<T> controllerClass, Environment execEnvironment) {
		LogHelper.debug( logger, "Created %s Request: '%s'", request.getClass(), request);
		
		S response = null;
		T controller = null;
		try {
			Class<?>[] parameterTypes = new Class<?>[] { request.getClass() };
			Constructor<T> constructor = controllerClass.getConstructor(parameterTypes);
			Object controllerObject = constructor.newInstance(request);
			controller = (T) controllerObject;
			ANetApiResponse baseResponse = controller.executeWithApiResponse(execEnvironment);
			LogHelper.info( logger, "%s ResultCode: %s", controllerClass, controller.getResultCode());
			LogHelper.info( logger, "%s Results:    %s", controllerClass, controller.getResults());
			response = (S) baseResponse;
			
		} catch (Exception e) {
			LogHelper.error(logger, "Exception : '%s' during %s", e.getMessage(), controllerClass);
		}
		Assert.assertEquals( MessageTypeEnum.OK, controller.getResultCode());
		Assert.assertNull(controller.getErrorResponse());
		Assert.assertNotNull(response);
		
		return response;
	}
}

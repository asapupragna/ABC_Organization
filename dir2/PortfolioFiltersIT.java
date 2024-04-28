package com.snc.it.ea_workspace.home;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import com.snc.glide.it.runners.Step;
import com.snc.it.ea_workspace.utils.WorkspaceConstants;
import com.snc.it.ea_workspace.utils.WorkspaceTestBase;
import com.snc.sdlc.annotations.TestCase;
import com.snc.selenium.runner.GlideUiRunner;

@RunWith(GlideUiRunner.class)
public class PortfolioFiltersIT extends WorkspaceTestBase implements WorkspaceConstants {
	
	private String apmUserName = "apmUser " + apmUtils.getRandomWord();
	private String apmUserId,query;
	private String retired_or_eol_business_app_query = "^install_status!=3^ORinstall_statusISEMPTY^life_cycle_stage!=End of Life^ORlife_cycle_stageISEMPTY";
	private String pushCode = "push";

	@BeforeClass
	public void setup() {
		apmUserId = apmUtils.createUserWithRole(apmUserName.replace(" ", "."), getRoleId(APM_USER));
	}

	@AfterClass
	public void tearDown() {
		apmUtils.deleteData(TABLE_SYS_USER, FIELD_SYSID, apmUserId);
	}
	
	@TestCase("TNTC0258559")
	@Step(value = 1, info = "Verify Application category")
	public void verifyApplicationCategoryFilter() throws JSONException, InterruptedException{
		sec.impersonate2(apmUserName);
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Accounts Payable");
		availableItems.add("Business Intelligence - ETL");
		assertEquals("Filter name is wrong",APPLICATION_CATEGORY,homePage.getFilterName("1"));
		homePage.applyFilter("1", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(2),homePage.getFilterPillCount("1"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="application_category=3e44fa32210e3b00964f98b7f95cf8a1^ORapplication_category=7b7b84fbdb6712003b9cffefbf961951"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());
		
	}
	
	@TestCase("TNTC0258559")
	@Step(value = 2, info = "Verify Install type filter")
	public void verifyInstallTypeFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Cloud");

		assertEquals("Filter name is wrong",INSTALL_TYPE,homePage.getFilterName("2"));
		homePage.applyFilter("2", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("2"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="install_type=cloud"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 3, info = "Verify Application type filter")
	public void verifyApplicationTypeFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("COTS");

		assertEquals("Filter name is wrong",APPLICATION_TYPE,homePage.getFilterName("3"));
		homePage.applyFilter("3", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("3"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="application_type=cots"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 4, info = "Verify Business Unit filter")
	public void verifyBusinessUnitFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Asia");

		assertEquals("Filter name is wrong",BUSINESS_UNIT,homePage.getFilterName("4"));
		homePage.applyFilter("4", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("4"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="business_unit=41c03c68db7712005d2cf78dbf961998"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 5, info = "Verify Business owner filter")
	public void verifyBusinessOwnerFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Abel Tuter (enterprise architect)");

		assertEquals("Filter name is wrong",BUSINESS_OWNER,homePage.getFilterName("5"));
		homePage.applyFilter("5", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("5"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="owned_by=62826bf03710200044e0bfc8bcbe5df1"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 6, info = "Verify Business owner filter")
	public void verifyITAppOwnerFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Abel Tuter (enterprise architect)");

		assertEquals("Filter name is wrong",IT_APPLICATION_OWNER,homePage.getFilterName("6"));
		homePage.applyFilter("6", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("6"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(1), homePage.getCardFilterCount("1"));
		query="it_application_owner=62826bf03710200044e0bfc8bcbe5df1"+retired_or_eol_business_app_query;
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("2"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getDonutFilterCount());
		if(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)==0)
			assertEquals("Message is incorrect","No data available.There is no data available for the selected criteria.",homePage.getDonutTextWhenZeroBA());
		else
			assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 7, info = "Verify Capability owner filter")
	public void verifyCapbilityOwnerFilter() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		List<String> availableItems = new ArrayList<String>();
		availableItems.add("Barbara Hindley");

		assertEquals("Filter name is wrong",CAPABILITY_OWNER,homePage.getFilterName("7"));
		homePage.applyFilter("7", availableItems);
		assertEquals("Filter pill count is incorrect",Long.valueOf(1),homePage.getFilterPillCount("7"));
		
		//Overview Cards
		
		assertEquals("Filter is not applied", Long.valueOf(0), homePage.getCardFilterCount("1"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,retired_or_eol_business_app_query)),homePage.getOverviewCardCount("1"));
		
		assertEquals("Filter is applied", Long.valueOf(1), homePage.getCardFilterCount("2"));
		query="owned_by=d2826bf03710200044e0bfc8bcbe5dc9";
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_CAPABILITIES,query)),homePage.getOverviewCardCount("2"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("3"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_INFO_OBJECT)),homePage.getOverviewCardCount("3"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("4"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusinessAppWithLowScore(TABLE_APP_APM_SCORE)),homePage.getOverviewCardCount("4"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getCardFilterCount("5"));
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getBusAppWithTRMTechnicalDebt()),homePage.getOverviewCardCount("5"));
		
		assertEquals("Filter is applied", Long.valueOf(0), homePage.getDonutFilterCount());
		assertEquals("Card count after filter applied is incorrect",String.valueOf(getRecordCount(TABLE_BUSINESS_APPLICATIONS,retired_or_eol_business_app_query)),homePage.getDonutTotal());

	}
	
	@TestCase("TNTC0258559")
	@Step(value = 8, info = "Verify multiple filter applied")
	public void verifyMultipleFilterApplied() throws JSONException, InterruptedException{
		open(APM_HOME_PAGE);
		homePage.clickHealthCard("1");
		listPage.clickFilterIcon();
		listPage.clickAdvancedViewBtn();
		String currentQuery=listPage.getCurrentEncodedFilterQuery();
		listPage.addFilter(currentQuery+"^application_category=b75b48fbdb6712003b9cffefbf961940^install_type=cloud");
		listPage.clickUpdate();
		String expectedCardCount = listPage.getBadgeCountInList();
		open(APM_HOME_PAGE);
		List<String> availableItems1 = new ArrayList<String>();
		availableItems1.add("Sourcing");
		List<String> availableItems2 = new ArrayList<String>();
		availableItems2.add("Cloud");
		homePage.applyFilter("1", availableItems1);
		homePage.applyFilter("2", availableItems2);
		assertEquals("card count mismatch",expectedCardCount,homePage.getHealthCardCount("1"));
	}
}

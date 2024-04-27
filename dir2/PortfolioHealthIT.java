package com.snc.it.ea_workspace.home;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;

import com.glide.script.GlideRecord;
import com.snc.glide.it.rules.DataLoader;
import com.snc.glide.it.runners.Step;
import com.snc.it.ea_workspace.utils.WorkspaceConstants;
import com.snc.it.ea_workspace.utils.WorkspaceTestBase;
import com.snc.sdlc.annotations.Story;
import com.snc.sdlc.annotations.TestCase;
import com.snc.selenium.runner.GlideUiRunner;

@Story("STRY54610360")
@RunWith(GlideUiRunner.class)
public class PortfolioHealthIT extends WorkspaceTestBase implements WorkspaceConstants {
	
	private String apmUserName = "apmUser " + apmUtils.getRandomWord();
	private String apmUserId,busApp1,busApp2,busApp3,busApp4,busApp5,busCap1,busCap2;
	private long busApp_count,busCap_count,busAppWithOutCap_count,busAppWithOutOwners_count,busAppNotAssessed_count,busAppWithOutAS_count,busAppWithOutAA_count,busCapWithOutBusApp_count,busCapNotAssessed_count;
	public static final DataLoader fDataLoader = new DataLoader();
	private String busAppNotRetriedQuery = "life_cycle_stage!=End of Life^ORlife_cycle_stageISEMPTY^install_status!=3^ORinstall_statusISEMPTY";
	
	@BeforeClass
	public void setup() {
		apmUserId = apmUtils.createUserWithRole(apmUserName.replace(" ", "."), getRoleId(APM_USER));
		apmUtils.executeTPMRiskParametersJob();
		busApp1=apmUtils.getRandomWord();
		busApp2=apmUtils.getRandomWord();
		busApp3=apmUtils.getRandomWord();
		busApp4=apmUtils.getRandomWord();
		busApp5=apmUtils.getRandomWord();
		busCap1=apmUtils.getRandomWord();
		busCap2=apmUtils.getRandomWord();
	}
	
	@AfterClass
	public void tearDown() {
		apmUtils.deleteData(TABLE_BUSINESS_APPLICATIONS, FIELD_NAME, busApp1);
		apmUtils.deleteData(TABLE_BUSINESS_APPLICATIONS, FIELD_NAME, busApp2);
		apmUtils.deleteData(TABLE_BUSINESS_APPLICATIONS, FIELD_NAME, busApp3);
		apmUtils.deleteData(TABLE_BUSINESS_APPLICATIONS, FIELD_NAME, busApp4);
		apmUtils.deleteData(TABLE_BUSINESS_APPLICATIONS, FIELD_NAME, busApp5);
		apmUtils.deleteData(TABLE_BUSINESS_CAPABILITIES, FIELD_NAME, busCap1);
		apmUtils.deleteData(TABLE_BUSINESS_CAPABILITIES, FIELD_NAME, busCap2);
		apmUtils.deleteData(TABLE_SYS_USER, FIELD_SYSID, apmUserId);
		fDataLoader.unloadResource("src//test//resources//HealthData//cmdb_ci_query_based_service.xml");
		fDataLoader.unloadResource("src//test//resources//HealthData//cmdb_ci_service_auto.xml");
		fDataLoader.unloadResource("src//test//resources//HealthData//cmdb_ci_service_discovered.xml");
	}
	
	@Step(value = 1, info = "Verify no of health cards without DI installed")
	public void verifyNoOfHealthCardsWithOutDI() {
		sec.impersonate2(apmUserName);
		open(APM_HOME_PAGE);
		assertEquals("No of health cards without DI is incorrect",7,homePage.getNoOfHealthCards());
	}

	@TestCase({"TNTC0261390","TNTC0324110"})
	@Step(value = 2, info = "Verify business applications without capabilities count is shown correctly on Health card")
	public void verifyBusAppWithoutCap() {
		open(APM_HOME_PAGE);
		assertEquals("First health card title is incorrect",BUS_APP_WITHOUT_CAP,homePage.getHealthCardTitle("1"));
		busApp_count=getRecordCount(TABLE_BUSINESS_APPLICATIONS,busAppNotRetriedQuery);
		busAppWithOutCap_count=busApp_count-getHealthData("cmdb_rel_ci","parent.sys_class_name=cmdb_ci_business_capability^child.sys_class_name=cmdb_ci_business_app^type=4afd799338a02000c18673032c71b817^child.life_cycle_stage!=End of Life^ORchild.life_cycle_stageISEMPTY^child.install_status!=3^ORchild.install_statusISEMPTY","child");
		assertEquals("Business applications w/o capablity count is incorrect",String.valueOf(busAppWithOutCap_count),homePage.getHealthCardCount("1"));
		long percentage = Math.round(((double)(busAppWithOutCap_count*100))/busApp_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Applications",homePage.getHealthCardBottonMessage("1"));
		apmUtils.createBusinessApplication(busApp1);
		busAppWithOutCap_count=busAppWithOutCap_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business applications w/o capablity count is incorrect",String.valueOf(busAppWithOutCap_count),homePage.getHealthCardCount("1"));
		homePage.clickHealthCard("1");
		assertEquals("List title is incorrect: ",BUS_APP_WITHOUT_CAP,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busAppWithOutCap_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		LOGGER.info("Fields: "+fields.subList(1, fields.size()).toString());
		assertEquals("Fields are mismatching",LIST_BUSINESS_APPLICATION_FILEDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261398","TNTC0324110"})
	@Step(value = 3, info = "Verify business applications without owners count is shown correctly on Health card")
	public void verifyBusAppWithoutOwners() {
		open(APM_HOME_PAGE);
		assertEquals("Second health card title is incorrect",BUS_APP_WITHOUT_OWNERS,homePage.getHealthCardTitle("2"));
		busAppWithOutOwners_count=getHealthData(TABLE_BUSINESS_APPLICATIONS,"it_application_owner=NULL^owned_by=NULL^"+busAppNotRetriedQuery,"");
		assertEquals("Business applications w/o owners count is incorrect",String.valueOf(busAppWithOutOwners_count),homePage.getHealthCardCount("2"));
		busApp_count=getRecordCount(TABLE_BUSINESS_APPLICATIONS,busAppNotRetriedQuery);
		long percentage = Math.round((double)(busAppWithOutOwners_count*100)/busApp_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Applications",homePage.getHealthCardBottonMessage("2"));
		apmUtils.createBusinessApplication(busApp2,null);
		busAppWithOutOwners_count=busAppWithOutOwners_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business applications w/o capablity count is incorrect",String.valueOf(busAppWithOutOwners_count),homePage.getHealthCardCount("2"));
		homePage.clickHealthCard("2");
		assertEquals("List title is incorrect: ",BUS_APP_WITHOUT_OWNERS,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busAppWithOutOwners_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_APPLICATION_FILEDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261392","TNTC0324110"})
	@Step(value = 4, info = "Verify business applications not assessed count is shown correctly on Health card")
	public void verifyBusAppNotAssessed() {
		open(APM_HOME_PAGE);
		assertEquals("Third health card title is incorrect",BUS_APP_NOT_ASSESSED,homePage.getHealthCardTitle("3"));
		busApp_count=getRecordCount(TABLE_BUSINESS_APPLICATIONS,busAppNotRetriedQuery);
		busAppNotAssessed_count=busApp_count-getHealthData(TABLE_APP_APM_SCORE,"cmdb_ci_business_app.sys_class_name=cmdb_ci_business_app^cmdb_ci_business_app.life_cycle_stage!=End of Life^ORcmdb_ci_business_app.life_cycle_stageISEMPTY^cmdb_ci_business_app.install_status!=3^ORcmdb_ci_business_app.install_statusISEMPTY",TABLE_BUSINESS_APPLICATIONS);
		assertEquals("Business applications not assessed count is incorrect",String.valueOf(busAppNotAssessed_count),homePage.getHealthCardCount("3"));
		long percentage = Math.round(((double)(busAppNotAssessed_count*100))/busApp_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Applications",homePage.getHealthCardBottonMessage("3"));
		apmUtils.createBusinessApplication(busApp3);
		busAppNotAssessed_count=busAppNotAssessed_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business applications not assessed count is incorrect",String.valueOf(busAppNotAssessed_count),homePage.getHealthCardCount("3"));
		homePage.clickHealthCard("3");
		assertEquals("List title is incorrect: ",BUS_APP_NOT_ASSESSED,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busAppNotAssessed_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_APPLICATION_FILEDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261394","TNTC0324110"})
	@Step(value = 5, info = "Verify business applications without application services count is shown correctly on Health card")
	public void verifyBusAppWithoutAS() {
		fDataLoader.loadResource("src//test//resources//HealthData//cmdb_ci_query_based_service.xml");
		fDataLoader.loadResource("src//test//resources//HealthData//cmdb_ci_service_auto.xml");
		fDataLoader.loadResource("src//test//resources//HealthData//cmdb_ci_service_discovered.xml");
		fDataLoader.loadResource("src//test//resources//HealthData//cmdb_rel_ci.xml");
		open(APM_HOME_PAGE);
		assertEquals("Fourth health card title is incorrect",BUS_APP_WITHOUT_AS,homePage.getHealthCardTitle("4"));
		busApp_count=getRecordCount(TABLE_BUSINESS_APPLICATIONS,busAppNotRetriedQuery);
		busAppWithOutAS_count=busApp_count-getHealthData(TABLE_CMDB_REL_CI,"parent.sys_class_name=cmdb_ci_business_app^child.sys_class_name=cmdb_ci_service_auto^ORchild.sys_class_name=cmdb_ci_query_based_service^ORchild.sys_class_name=cmdb_ci_service_discovered^type=41008aa6ef32010098d5925495c0fb94^parent.life_cycle_stage!=End of Life^ORparent.life_cycle_stageISEMPTY^parent.install_status!=3^ORparent.install_statusISEMPTY","parent");
		assertEquals("Business applications Without applcation service count is incorrect",String.valueOf(busAppWithOutAS_count),homePage.getHealthCardCount("4"));
		long percentage = Math.round(((double)(busAppWithOutAS_count*100))/busApp_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Applications",homePage.getHealthCardBottonMessage("4"));
		apmUtils.createBusinessApplication(busApp4);
		busAppWithOutAS_count=busAppWithOutAS_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business applications without application service count is incorrect",String.valueOf(busAppWithOutAS_count),homePage.getHealthCardCount("4"));
		homePage.clickHealthCard("4");
		assertEquals("List title is incorrect: ",BUS_APP_WITHOUT_AS,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busAppWithOutAS_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_APPLICATION_FILEDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261396","TNTC0324110"})
	@Step(value = 6, info = "Verify business applications without architectural artifacts count is shown correctly on Health card")
	public void verifyBusAppWithoutArchitecturalArtifacts() {
		open(APM_HOME_PAGE);
		assertEquals("Fifth health card title is incorrect",BUS_APP_WITHOUT_ARCHITECTURAL_ARTIFACTS,homePage.getHealthCardTitle("5"));
		busApp_count=getRecordCount(TABLE_BUSINESS_APPLICATIONS,busAppNotRetriedQuery);
		busAppWithOutAA_count=busApp_count-getHealthData(TABLE_SN_APM_RELATED_ENTITIES_LIST,"table_name=cmdb_ci_business_app","target_record");
		assertEquals("Business applications Without architectural artifacts count is incorrect",String.valueOf(busAppWithOutAA_count),homePage.getHealthCardCount("5"));
		long percentage = Math.round(((double)(busAppWithOutAA_count*100))/busApp_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Applications",homePage.getHealthCardBottonMessage("5"));
		apmUtils.createBusinessApplication(busApp5);
		busAppWithOutAA_count=busAppWithOutAA_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business applications without architectural artifacts count is incorrect",String.valueOf(busAppWithOutAA_count),homePage.getHealthCardCount("5"));
		homePage.clickHealthCard("5");
		assertEquals("List title is incorrect: ",BUS_APP_WITHOUT_ARCHITECTURAL_ARTIFACTS,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busAppWithOutAA_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_APPLICATION_FILEDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261391","TNTC0324110"})
	@Step(value = 7, info = "Verify business capabilities without business applications count is shown correctly on Health card")
	public void verifyBusCapWithoutBusApp() {
		open(APM_HOME_PAGE);
		assertEquals("Sixth health card title is incorrect",BUS_CAP_WITHOUT_APP,homePage.getHealthCardTitle("6"));
		busCap_count=getRecordCount(TABLE_BUSINESS_CAPABILITIES);
		busCapWithOutBusApp_count=busCap_count-getHealthData(TABLE_CMDB_REL_CI,"parent.sys_class_name=cmdb_ci_business_capability^child.sys_class_name=cmdb_ci_business_app^type=4afd799338a02000c18673032c71b817","parent");
		assertEquals("Business capabilities without business applications count is incorrect",String.valueOf(busCapWithOutBusApp_count),homePage.getHealthCardCount("6"));
		long percentage = Math.round(((double)(busCapWithOutBusApp_count*100))/busCap_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Capabilities",homePage.getHealthCardBottonMessage("6"));
		apmUtils.createBusinessCapability(busCap1, null);
		busCapWithOutBusApp_count=busCapWithOutBusApp_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business capabilities without business applications count is incorrect",String.valueOf(busCapWithOutBusApp_count),homePage.getHealthCardCount("6"));
		homePage.clickHealthCard("6");
		assertEquals("List title is incorrect: ",BUS_CAP_WITHOUT_APP,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busCapWithOutBusApp_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_CAPABILITIES_FIELDS,fields.subList(1, fields.size()).toString());
	}
	
	@TestCase({"TNTC0261393","TNTC0324110"})
	@Step(value = 8, info = "Verify business capabilities not assessed count is shown correctly on Health card")
	public void verifyBusCapNotAssessed() {
		open(APM_HOME_PAGE);
		assertEquals("Seventh health card title is incorrect",BUS_CAP_NOT_ASSESSED,homePage.getHealthCardTitle("7"));
		busCap_count=getRecordCount(TABLE_BUSINESS_CAPABILITIES);
		busCapNotAssessed_count=busCap_count-getHealthData(TABLE_APP_APM_SCORE,"cmdb_ci_business_app.sys_class_name=cmdb_ci_business_capability^",TABLE_BUSINESS_APPLICATIONS);
		assertEquals("Business capabilities not assessed count is incorrect",String.valueOf(busCapNotAssessed_count),homePage.getHealthCardCount("7"));
		long percentage = Math.round(((double)(busCapNotAssessed_count*100))/busCap_count);
		assertEquals("Bottom message is incoreect",percentage+"% of all Capabilities",homePage.getHealthCardBottonMessage("7"));
		apmUtils.createBusinessCapability(busCap2, null);
		busCapNotAssessed_count=busCapNotAssessed_count+1;
		open(APM_HOME_PAGE);
		assertEquals("Business capabilities not assessed count is incorrect",String.valueOf(busCapNotAssessed_count),homePage.getHealthCardCount("7"));
		homePage.clickHealthCard("7");
		assertEquals("List title is incorrect: ",BUS_CAP_NOT_ASSESSED,listPage.getListTitle());
		assertEquals("Badge count is incorrect: ",String.valueOf(busCapNotAssessed_count),listPage.getBadgeCountInList());
		List<String>fields=listPage.getColumnNames(listPage.TAB_NOW_RECORD_LIST_LOC);
		assertEquals("Fields are mismatching",LIST_BUSINESS_CAPABILITIES_FIELDS,fields.subList(1, fields.size()).toString());
	}
}

package prioritization.data_objects;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Stores all faulty version IDs of the train set per project.
 */
public class TrainSet {
	
	private Map<String, Set<String>> faultyVersions;
	
	public TrainSet() {
		faultyVersions = new HashMap<String, Set<String>>();
		initBiojavaFaultyVersions();
		initCommonsCollectionsFaultyVersions();
		initJsoupFaultyVersions();
		initUrbanAirshipFaultyVersions();
		initLittleproxyFaultyVersions();
	}
	private void initBiojavaFaultyVersions() {
		Set<String> biojavaVersions = new HashSet<String>(Arrays.asList("biojava-2","biojava-4","biojava-7","biojava-11","biojava-15","biojava-16",
				"biojava-201","biojava-22","biojava-23","biojava-24","biojava-204","biojava-32","biojava-33","biojava-40",
				"biojava-208","biojava-42","biojava-43","biojava-47","biojava-53","biojava-54","biojava-59","biojava-212",
				"biojava-213","biojava-61","biojava-63","biojava-70","biojava-72","biojava-74","biojava-76","biojava-216",
				"biojava-219","biojava-83","biojava-86","biojava-88","biojava-91","biojava-94","biojava-96","biojava-223",
				"biojava-226","biojava-108","biojava-109","biojava-110","biojava-111","biojava-115","biojava-120",
				"biojava-230","biojava-232","biojava-123","biojava-125","biojava-128","biojava-131","biojava-135",
				"biojava-138","biojava-235","biojava-238","biojava-144","biojava-146","biojava-148","biojava-156",
				"biojava-157","biojava-158","biojava-240","biojava-244","biojava-161","biojava-166","biojava-168",
				"biojava-173","biojava-175","biojava-178","biojava-246","biojava-251","biojava-182","biojava-185",
				"biojava-188","biojava-193","biojava-196","biojava-200","biojava-252","biojava-255"));
		faultyVersions.put("biojava", biojavaVersions);
	}
	private void initCommonsCollectionsFaultyVersions() {
		Set<String> commonsCollectionsProjects = new HashSet<String>(Arrays.asList("commons-collections-3","commons-collections-4",
				"commons-collections-5","commons-collections-12","commons-collections-14","commons-collections-16",
				"commons-collections-202","commons-collections-21","commons-collections-28","commons-collections-29",
				"commons-collections-33","commons-collections-36","commons-collections-37","commons-collections-205",
				"commons-collections-209","commons-collections-44","commons-collections-48","commons-collections-49",
				"commons-collections-51","commons-collections-54","commons-collections-55","commons-collections-210",
				"commons-collections-215","commons-collections-62","commons-collections-65","commons-collections-67",
				"commons-collections-72","commons-collections-75","commons-collections-77","commons-collections-217",
				"commons-collections-221","commons-collections-84","commons-collections-86","commons-collections-89",
				"commons-collections-92","commons-collections-95","commons-collections-99","commons-collections-223",
				"commons-collections-225","commons-collections-105","commons-collections-106","commons-collections-110",
				"commons-collections-113","commons-collections-114","commons-collections-116","commons-collections-228",
				"commons-collections-231","commons-collections-123","commons-collections-128","commons-collections-129",
				"commons-collections-133","commons-collections-136","commons-collections-138","commons-collections-235",
				"commons-collections-237","commons-collections-142","commons-collections-145","commons-collections-147",
				"commons-collections-152","commons-collections-153","commons-collections-158","commons-collections-241",
				"commons-collections-245","commons-collections-164","commons-collections-167","commons-collections-169",
				"commons-collections-174","commons-collections-178","commons-collections-180","commons-collections-247",
				"commons-collections-250","commons-collections-182","commons-collections-185","commons-collections-187",
				"commons-collections-193","commons-collections-198","commons-collections-200","commons-collections-254",
				"commons-collections-257"));
		faultyVersions.put("commons-collections", commonsCollectionsProjects);
	}
	private void initJsoupFaultyVersions() {
		Set<String> jsoupProjects = new HashSet<String>(Arrays.asList("jsoup-1","jsoup-4","jsoup-8","jsoup-11","jsoup-13","jsoup-15",
				"jsoup-22","jsoup-24","jsoup-25","jsoup-32","jsoup-34","jsoup-36","jsoup-43","jsoup-47","jsoup-50",
				"jsoup-52","jsoup-54","jsoup-57","jsoup-61","jsoup-64","jsoup-69","jsoup-71","jsoup-74","jsoup-80",
				"jsoup-82","jsoup-86","jsoup-89","jsoup-94","jsoup-98","jsoup-99","jsoup-102","jsoup-103","jsoup-106",
				"jsoup-112","jsoup-115","jsoup-118","jsoup-121","jsoup-124","jsoup-128","jsoup-136","jsoup-137",
				"jsoup-139","jsoup-142","jsoup-144","jsoup-145","jsoup-152","jsoup-158","jsoup-160","jsoup-162",
				"jsoup-167","jsoup-169","jsoup-171","jsoup-176","jsoup-180","jsoup-183","jsoup-185","jsoup-188",
				"jsoup-191","jsoup-194","jsoup-199","jsoup-201","jsoup-204","jsoup-208","jsoup-210","jsoup-213",
				"jsoup-218","jsoup-221","jsoup-223","jsoup-226","jsoup-230","jsoup-232","jsoup-234","jsoup-237",
				"jsoup-241","jsoup-245","jsoup-247","jsoup-251","jsoup-252","jsoup-255"));
		faultyVersions.put("jsoup", jsoupProjects);
	}
	private void initLittleproxyFaultyVersions() {
		Set<String> littleproxyProjects = new HashSet<String>(Arrays.asList("littleproxy-3","littleproxy-4","littleproxy-5","littleproxy-12",
				"littleproxy-16","littleproxy-17","littleproxy-23","littleproxy-27","littleproxy-30","littleproxy-33",
				"littleproxy-37","littleproxy-39","littleproxy-44","littleproxy-46","littleproxy-50","littleproxy-52",
				"littleproxy-54","littleproxy-55","littleproxy-63","littleproxy-66","littleproxy-68","littleproxy-74",
				"littleproxy-77","littleproxy-79","littleproxy-85","littleproxy-87","littleproxy-89","littleproxy-91",
				"littleproxy-95","littleproxy-99","littleproxy-102","littleproxy-105","littleproxy-110","littleproxy-114",
				"littleproxy-116","littleproxy-119","littleproxy-121","littleproxy-123","littleproxy-125","littleproxy-132",
				"littleproxy-134","littleproxy-136","littleproxy-142","littleproxy-145","littleproxy-150","littleproxy-151",
				"littleproxy-156","littleproxy-158","littleproxy-160","littleproxy-164","littleproxy-166","littleproxy-169",
				"littleproxy-172","littleproxy-175","littleproxy-178","littleproxy-183","littleproxy-184","littleproxy-189",
				"littleproxy-190","littleproxy-195","littleproxy-197"));
		faultyVersions.put("littleproxy", littleproxyProjects);
	}
	
	private void initUrbanAirshipFaultyVersions() {
		Set<String> urbanAirshipProjects = new HashSet<String>(Arrays.asList("urban-airship-5","urban-airship-6","urban-airship-8",
				"urban-airship-15","urban-airship-16","urban-airship-18","urban-airship-202","urban-airship-22",
				"urban-airship-26","urban-airship-29","urban-airship-32","urban-airship-35","urban-airship-38",
				"urban-airship-206","urban-airship-208","urban-airship-41","urban-airship-47","urban-airship-48",
				"urban-airship-53","urban-airship-56","urban-airship-60","urban-airship-211","urban-airship-214",
				"urban-airship-63","urban-airship-65","urban-airship-69","urban-airship-73","urban-airship-75",
				"urban-airship-80","urban-airship-216","urban-airship-219","urban-airship-83","urban-airship-86",
				"urban-airship-89","urban-airship-92","urban-airship-96","urban-airship-100","urban-airship-223",
				"urban-airship-225","urban-airship-105","urban-airship-106","urban-airship-110","urban-airship-111",
				"urban-airship-113","urban-airship-119","urban-airship-229","urban-airship-231","urban-airship-122",
				"urban-airship-124","urban-airship-128","urban-airship-135","urban-airship-137","urban-airship-140",
				"urban-airship-235","urban-airship-239","urban-airship-143","urban-airship-147","urban-airship-150",
				"urban-airship-153","urban-airship-155","urban-airship-157","urban-airship-240","urban-airship-245",
				"urban-airship-162","urban-airship-165","urban-airship-167","urban-airship-171","urban-airship-174",
				"urban-airship-179","urban-airship-248","urban-airship-250","urban-airship-185","urban-airship-187",
				"urban-airship-189","urban-airship-191","urban-airship-193","urban-airship-197","urban-airship-253",
				"urban-airship-255"));
		faultyVersions.put("urban-airship", urbanAirshipProjects);
	}
	
	public boolean containsProject(String projectName) {
		return faultyVersions.containsKey(projectName);
	}
	/**
	 * Returns all passed faulty version that are contained in the train set.
	 */
	public void retainOnlyTrainSetVersions(List<FaultyVersion> faultyVersions){
		Iterator<FaultyVersion> iter = faultyVersions.iterator();
		while (iter.hasNext()) {
			FaultyVersion next = iter.next();
			Set<String> projectTrainSetIds = this.faultyVersions.get(next.getProjectMetrics().getProjectName());
			if (projectTrainSetIds != null) {
				if (projectTrainSetIds.contains(next.getProjectMetrics().getId())) {
					// version is contained in the train set, retain it
					continue;
				}
			}
			iter.remove();
		}
	}
	/**
	 * Returns all passed faulty version that are not contained in the train set.
	 */
	public void filterOutTrainSetVersions(List<FaultyVersion> faultyVersions) {
		Iterator<FaultyVersion> iter = faultyVersions.iterator();
		while (iter.hasNext()) {
			FaultyVersion next = iter.next();
			Set<String> projectTrainSetIds = this.faultyVersions.get(next.getProjectMetrics().getProjectName());
			if (projectTrainSetIds != null) {
				if (projectTrainSetIds.contains(next.getProjectMetrics().getId())) {
					// version is contained in the train set, remove it
					iter.remove();
				}
			}
		}
	}
}

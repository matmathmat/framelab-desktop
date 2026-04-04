package fr.framelab;

import org.junit.platform.suite.api.ExcludeClassNamePatterns;
import org.junit.platform.suite.api.SelectPackages;
import org.junit.platform.suite.api.Suite;

@Suite
@SelectPackages("fr.framelab")
@ExcludeClassNamePatterns("fr.framelab.AllTests")
public class AllTests {}
package net.karneim.pojobuilder.analysis.with.superclass;

import static org.assertj.core.api.Assertions.assertThat;

import javax.annotation.processing.ProcessingEnvironment;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import net.karneim.pojobuilder.analysis.DirectivesFactory;
import net.karneim.pojobuilder.analysis.Input;
import net.karneim.pojobuilder.analysis.InputFactory;
import net.karneim.pojobuilder.analysis.JavaModelAnalyzer;
import net.karneim.pojobuilder.analysis.JavaModelAnalyzerUtil;
import net.karneim.pojobuilder.analysis.Output;
import net.karneim.pojobuilder.model.PropertyListM.Key;
import net.karneim.pojobuilder.model.PropertyM;
import net.karneim.pojobuilder.model.TypeM;
import net.karneim.pojobuilder.testenv.AddToSourceTree;
import net.karneim.pojobuilder.testenv.ProcessingEnvironmentRunner;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ProcessingEnvironmentRunner.class)
@AddToSourceTree({"src/testdata/java"})
public class JavaModelAnalyzer_PojoWithSuperclass_Test {

  private ProcessingEnvironment env;
  private Elements elements;
  private InputFactory inputFactory;
  private JavaModelAnalyzer underTest;

  @Before
  public void init() {
    env = ProcessingEnvironmentRunner.getProcessingEnvironment();
    elements = env.getElementUtils();
    JavaModelAnalyzerUtil javaModelAnalyzerUtil = new JavaModelAnalyzerUtil(env.getElementUtils(), env.getTypeUtils());
    inputFactory =
        new InputFactory(env.getTypeUtils(), new DirectivesFactory(env.getElementUtils(), env.getTypeUtils(),
            javaModelAnalyzerUtil));
    underTest =
        new JavaModelAnalyzer(env.getElementUtils(), env.getTypeUtils(), new JavaModelAnalyzerUtil(
            env.getElementUtils(), env.getTypeUtils()));
  }

  @Test
  public void testAnalyzeWithPojoExtendsClassWithPublicField() throws Exception {
    // Given:
    String pojoClassname = SubclassPojo1.class.getCanonicalName();
    TypeElement pojoType = elements.getTypeElement(pojoClassname);
    Input input = inputFactory.getInput(pojoType);

    // When:
    Output output = underTest.analyze(input);

    // Then:
    assertThat(output).isNotNull();
    assertThat(output.getBuilderModel().getPojoType().getName()).isEqualTo(pojoClassname);
    TypeM builderType = output.getBuilderModel().getType();
    assertThat(builderType).isNotNull();
    assertThat(builderType.getName())
        .isEqualTo("net.karneim.pojobuilder.analysis.with.superclass.SubclassPojo1Builder");
    assertThat(output.getBuilderModel().getProperties()).hasSize(3);
    PropertyM nameProperty = output.getBuilderModel().getProperties().get(new Key("name", "java.lang.String"));
    assertThat(nameProperty).isNotNull();
    assertThat(nameProperty.getFieldAccess()).isNotNull();
    assertThat(nameProperty.getFieldAccess().getModifier()).contains(Modifier.PUBLIC);

    PropertyM ageProperty = output.getBuilderModel().getProperties().get(new Key("age", "int"));
    assertThat(ageProperty).isNotNull();
    assertThat(ageProperty.getFieldAccess()).isNotNull();
    assertThat(ageProperty.getFieldAccess().getModifier()).contains(Modifier.PUBLIC);

    PropertyM hairColorProperty = output.getBuilderModel().getProperties().get(new Key("hairColor", "java.awt.Color"));
    assertThat(hairColorProperty).isNotNull();
    assertThat(hairColorProperty.getFieldAccess()).isNotNull();
    assertThat(hairColorProperty.getFieldAccess().getModifier()).contains(Modifier.PUBLIC);
  }

  @Test
  public void testAnalyzeWithPojoWhichExtendsClassInSamePackage() throws Exception {
    // Given:
    String pojoClassname = SubclassPojo2.class.getCanonicalName();
    TypeElement pojoType = elements.getTypeElement(pojoClassname);
    Input input = inputFactory.getInput(pojoType);

    // When:
    Output output = underTest.analyze(input);

    // Then:
    assertThat(output).isNotNull();
    assertThat(output.getBuilderModel().getPojoType().getName()).isEqualTo(pojoClassname);
    TypeM builderType = output.getBuilderModel().getType();
    assertThat(builderType).isNotNull();
    assertThat(builderType.getName())
        .isEqualTo("net.karneim.pojobuilder.analysis.with.superclass.SubclassPojo2Builder");
    assertThat(output.getBuilderModel().getProperties()).hasSize(3);
    PropertyM visibleMemberProperty = output.getBuilderModel().getProperties().get(new Key("visibleMember", "int"));
    assertThat(visibleMemberProperty).isNotNull();
    assertThat(visibleMemberProperty.getFieldAccess()).isNotNull();
    assertThat(visibleMemberProperty.getFieldAccess().getModifier()).contains(Modifier.PUBLIC);
    
    PropertyM protectedMemberProperty = output.getBuilderModel().getProperties().get(new Key("protectedMember", "float"));
    assertThat(protectedMemberProperty).isNotNull();
    assertThat(protectedMemberProperty.getFieldAccess()).isNotNull();
    assertThat(protectedMemberProperty.getFieldAccess().getModifier()).contains(Modifier.PROTECTED);
    
    PropertyM nameMemberProperty = output.getBuilderModel().getProperties().get(new Key("name", "java.lang.String"));
    assertThat(nameMemberProperty).isNotNull();
    assertThat(nameMemberProperty.getFieldAccess()).isNull();
    assertThat(nameMemberProperty.getGetterMethod().getModifier()).contains(Modifier.PROTECTED);
    assertThat(nameMemberProperty.getSetterMethod().getModifier()).contains(Modifier.PROTECTED);
    
    assertThat(output.getBuilderModel().getProperties().get(new Key("hiddenMember", "float"))).isNull();
    

  }
  
  @Test
  public void testAnalyzeWithPojoWhichExtendsClassInOtherPackage() throws Exception {
    // Given:
    String pojoClassname = SubclassPojo3.class.getCanonicalName();
    TypeElement pojoType = elements.getTypeElement(pojoClassname);
    Input input = inputFactory.getInput(pojoType);

    // When:
    Output output = underTest.analyze(input);

    // Then:
    assertThat(output).isNotNull();
    assertThat(output.getBuilderModel().getPojoType().getName()).isEqualTo(pojoClassname);
    TypeM builderType = output.getBuilderModel().getType();
    assertThat(builderType).isNotNull();
    assertThat(builderType.getName())
        .isEqualTo("net.karneim.pojobuilder.analysis.with.superclass.SubclassPojo3Builder");
    assertThat(output.getBuilderModel().getProperties()).hasSize(1);
    PropertyM visibleMemberProperty = output.getBuilderModel().getProperties().get(new Key("visibleMember", "int"));
    assertThat(visibleMemberProperty).isNotNull();
    assertThat(visibleMemberProperty.getFieldAccess()).isNotNull();
    assertThat(visibleMemberProperty.getFieldAccess().getModifier()).contains(Modifier.PUBLIC);
    assertThat(output.getBuilderModel().getProperties().get(new Key("hiddenMember", "float"))).isNull();;

  }



}

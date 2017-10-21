#version 120

//This only does one-sided lighting
//with one positional light
//This uses Blinn Phong Specular calculations
//You need to make sure calculations are all done in the same
//coordinate system
//Usually easiest to do in eye/camera coordinates
void main(void) {
	vec3 normal, v,lightDir;
	vec4 diffuse, ambient, globalAmbient;
	float NdotL;
	
	// Compute the ambient and globalAmbient terms 
	globalAmbient = gl_LightModel.ambient * gl_FrontMaterial.ambient;
	ambient =  gl_LightSource[0].ambient * gl_FrontMaterial.ambient;
		
	// Compute the diffuse term 
	
	// Convert vertexPos into eye coords
	 v = vec3(gl_ModelViewMatrix * gl_Vertex);
	 
	//Convert normal into eye coords
	normal = normalize(gl_NormalMatrix * gl_Normal);
	
	// Assuming point light, get a vector TO the light
	// Note: LightSource is given in camera coords
	lightDir = normalize(gl_LightSource[0].position.xyz - v);
	
	//Lambert equation
	NdotL = max(dot(normal, lightDir), 0.0);
	
	//Multiply by RGBA material coefficients and lightSource intensities
	diffuse = NdotL * gl_FrontMaterial.diffuse * gl_LightSource[0].diffuse;

    //Specular lighting assuming we are using Blinn Phong with Half Vector
    float NdotHV;  
    
    vec3 dirToView = normalize(-v);
    vec3 H =  normalize(dirToView+lightDir); 
   
    // compute the specular term if NdotL is  larger than zero, otherwise leave it as 0
 
    vec4 specular = vec4(0.0,0.0,0.0,1); 

	if (NdotL > 0.0) {
		
		NdotHV = max(dot(normal,H) ,0.0);
		
		specular = gl_FrontMaterial.specular * gl_LightSource[0].specular * pow(NdotHV,gl_FrontMaterial.shininess);
	}
    
	gl_FrontColor = gl_FrontMaterial.emission + globalAmbient + ambient + diffuse + specular;
		
	gl_Position = gl_ModelViewProjectionMatrix * gl_Vertex;
} 
